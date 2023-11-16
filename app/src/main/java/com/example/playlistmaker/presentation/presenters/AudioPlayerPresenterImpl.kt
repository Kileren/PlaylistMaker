package com.example.playlistmaker.presentation.presenters

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.AudioPlayerInteractor
import com.example.playlistmaker.domain.api.Player
import com.example.playlistmaker.domain.impl.PlayerState
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.api.AudioPlayer
import com.example.playlistmaker.presentation.api.AudioPlayerPresenter
import com.example.playlistmaker.presentation.mappers.TrackMapper
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerPresenterImpl(
    private val audioPlayer: AudioPlayer,
    private val audioPlayerInteractor: AudioPlayerInteractor
): AudioPlayerPresenter, AudioPlayerInteractor.AudioPlayerConsumer, Player.StateListener {

    companion object {
        private const val PLAYER_PLAYBACK_REFRESH_DELAY = 300L
    }

    private val handler = Handler(Looper.getMainLooper())
    private var playerTimerRunnable = Runnable { refreshPlaybackTime() }
    private val playbackTimeFormatter = SimpleDateFormat("mm:ss", Locale.getDefault())

    override fun onCreate(intent: Intent, context: Context) {
        val trackID = intent.getStringExtra(AudioPlayer.trackKey)
        if (trackID == null) {
            assert(false) { "Track ID should be passed" }
            return
        }

        audioPlayerInteractor.setPlayerStateListener(this)
        audioPlayerInteractor.setPlayerCompletionListener {
            stopPlaybackTimeRefreshing()
            audioPlayer.updatePlaybackTime(context.getString(R.string.default_audio_playback_time))
        }
        audioPlayerInteractor.loadTrack(trackID, this)
    }

    override fun onPause() {
        if (audioPlayerInteractor.currentPlayerState == PlayerState.PLAYING) {
            audioPlayerInteractor.pausePlayer()
        }
    }

    override fun onDestroy() {
        audioPlayerInteractor.releasePlayer()
        stopPlaybackTimeRefreshing()
    }

    override fun consume(track: Track) {
        val trackInfo = TrackMapper.map(track)
        audioPlayer.configure(trackInfo)
    }

    override fun stateChanged(state: PlayerState) {
        when (state) {
            PlayerState.PLAYING -> audioPlayer.showPauseButton()
            PlayerState.PAUSED, PlayerState.PREPARED -> audioPlayer.showPlayButton()
            else -> return
        }
    }

    override fun playButtonTapped() {
        when (audioPlayerInteractor.currentPlayerState) {
            PlayerState.PLAYING -> {
                audioPlayerInteractor.pausePlayer()
                stopPlaybackTimeRefreshing()
            }
            PlayerState.PREPARED, PlayerState.PAUSED -> {
                audioPlayerInteractor.startPlayer()
                startPlaybackTimeRefreshing()
            }
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
        val time = playbackTimeFormatter.format(audioPlayerInteractor.currentPlayerPosition)
        audioPlayer.updatePlaybackTime(time)
        startPlaybackTimeRefreshing()
    }
}