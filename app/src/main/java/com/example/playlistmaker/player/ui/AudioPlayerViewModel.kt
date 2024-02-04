package com.example.playlistmaker.player.ui

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.library.domain.FavouriteTracksInteractor
import com.example.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.example.playlistmaker.player.domain.api.Player
import com.example.playlistmaker.player.domain.impl.PlayerState
import com.example.playlistmaker.player.domain.mappers.TrackMapper
import com.example.playlistmaker.player.ui.models.AudioPlayerPlayButtonState
import com.example.playlistmaker.player.ui.models.TrackInfo
import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerViewModel(
    private val interactor: AudioPlayerInteractor,
    private val favouriteTracksInteractor: FavouriteTracksInteractor
): ViewModel(), AudioPlayerInteractor.AudioPlayerConsumer, Player.StateListener {

    private val _trackInfo = MutableLiveData<TrackInfo>()
    val trackInfo: LiveData<TrackInfo> = _trackInfo

    private val _audioPlaybackModel = MutableLiveData<AudioPlaybackModel>()
    val audioPlaybackModel: LiveData<AudioPlaybackModel> = _audioPlaybackModel

    private var timerJob: Job? = null
    private val playbackTimeFormmatter = SimpleDateFormat("mm:ss", Locale.getDefault())

    fun onCreate(intent: Intent, context: Context) {
        val trackID = intent.getStringExtra(AudioPlayerActivity.trackKey)
        if (trackID == null) {
            assert(false) { "Track ID should be passed" }
            return
        }

        _audioPlaybackModel.postValue(AudioPlaybackModel(playbackTime = context.getString(R.string.default_audio_playback_time)))

        interactor.setPlayerStateListener(this)
        interactor.setPlayerCompletionListener {
            stopPlaybackTimeRefreshing()
            _audioPlaybackModel.postValue(
                audioPlaybackModel.value?.copy(
                    playbackTime = context.getString(R.string.default_audio_playback_time),
                    playButtonState = AudioPlayerPlayButtonState.PLAY
                )
            )
        }

        viewModelScope.launch {
            interactor.loadTrack(trackID, this@AudioPlayerViewModel)
        }
    }

    fun onPause() {
        if (interactor.currentPlayerState == PlayerState.PLAYING) {
            interactor.pausePlayer()
        }
    }

    fun onDestroy() {
        interactor.releasePlayer()
        stopPlaybackTimeRefreshing()
    }

    fun playButtonTapped() {
        when (interactor.currentPlayerState) {
            PlayerState.PLAYING -> {
                interactor.pausePlayer()
                stopPlaybackTimeRefreshing()
            }
            PlayerState.PREPARED, PlayerState.PAUSED -> {
                interactor.startPlayer()
                startPlaybackTimeRefreshing()
            }
            else -> return
        }
    }

    fun favouriteButtonTapped() {
        val trackId = trackInfo.value?.trackId
        if (trackId != null) {
            viewModelScope.launch {
                val track = interactor.getTrack(trackId)
                if (track.isFavourite) {
                    favouriteTracksInteractor.removeTrack(track)
                    _trackInfo.postValue(trackInfo.value?.copy(isFavourite = false))
                } else {
                    favouriteTracksInteractor.addTrack(track)
                    _trackInfo.postValue(trackInfo.value?.copy(isFavourite = true))
                }
            }
        }
    }

    override fun consume(track: Track) {
        val trackInfo = TrackMapper.map(track)
        this._trackInfo.postValue(trackInfo)
    }

    override fun stateChanged(state: PlayerState) {
        when (state) {
            PlayerState.PLAYING -> _audioPlaybackModel.postValue(
                audioPlaybackModel.value?.copy(playButtonState = AudioPlayerPlayButtonState.PAUSE)
            )
            PlayerState.PAUSED, PlayerState.PREPARED -> _audioPlaybackModel.postValue(
                audioPlaybackModel.value?.copy(playButtonState = AudioPlayerPlayButtonState.PLAY)
            )
            else -> return
        }
    }

    private fun startPlaybackTimeRefreshing() {
        timerJob = viewModelScope.launch {
            delay(PLAYER_PLAYBACK_REFRESH_DELAY)
            refreshPlaybackTime()
        }
    }

    private fun stopPlaybackTimeRefreshing() {
        timerJob?.cancel()
    }

    private fun refreshPlaybackTime() {
        val time = playbackTimeFormmatter.format(interactor.currentPlayerPosition)
        _audioPlaybackModel.postValue(audioPlaybackModel.value?.copy(playbackTime = time))
        startPlaybackTimeRefreshing()
    }

    companion object {
        private const val PLAYER_PLAYBACK_REFRESH_DELAY = 300L
    }
}