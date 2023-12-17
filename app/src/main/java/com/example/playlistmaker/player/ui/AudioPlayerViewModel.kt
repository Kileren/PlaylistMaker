package com.example.playlistmaker.player.ui

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.R
import com.example.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.example.playlistmaker.player.domain.api.Player
import com.example.playlistmaker.player.domain.impl.PlayerState
import com.example.playlistmaker.player.domain.mappers.TrackMapper
import com.example.playlistmaker.player.ui.models.AudioPlayerPlayButtonState
import com.example.playlistmaker.player.ui.models.TrackInfo
import com.example.playlistmaker.search.domain.Track
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerViewModel(
    private val interactor: AudioPlayerInteractor
): ViewModel(), AudioPlayerInteractor.AudioPlayerConsumer, Player.StateListener {

    private val _trackInfo = MutableLiveData<TrackInfo>()
    val trackInfo: LiveData<TrackInfo> = _trackInfo

    private val _audioPlaybackModel = MutableLiveData<AudioPlaybackModel>()
    val audioPlaybackModel: LiveData<AudioPlaybackModel> = _audioPlaybackModel

    private val handler = Handler(Looper.getMainLooper())
    private val playerTimerRunnable = Runnable { refreshPlaybackTime() }
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
        interactor.loadTrack(trackID, this)
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
        handler.postDelayed(playerTimerRunnable, PLAYER_PLAYBACK_REFRESH_DELAY)
    }

    private fun stopPlaybackTimeRefreshing() {
        handler.removeCallbacks(playerTimerRunnable)
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