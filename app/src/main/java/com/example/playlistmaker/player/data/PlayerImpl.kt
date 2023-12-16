package com.example.playlistmaker.player.data

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.api.Player
import com.example.playlistmaker.player.domain.impl.PlayerState

class PlayerImpl: Player {

    private val mediaPlayer = MediaPlayer()
    private var stateListener: Player.StateListener? = null
    private var completionListener: (() -> Unit)? = null

    override var state: PlayerState = PlayerState.DEFAULT
    override val currentPosition: Int
        get() = mediaPlayer.currentPosition

    override fun setUpPlayer(previewURL: String) {
        mediaPlayer.apply {
            setDataSource(previewURL)
            prepareAsync()
            setOnPreparedListener {
                changeStateTo(PlayerState.PREPARED)
            }
            setOnCompletionListener {
                changeStateTo(PlayerState.PREPARED)
                completionListener?.invoke()
            }
        }
    }

    override fun setStateListener(listener: Player.StateListener) {
        this.stateListener = listener
    }

    override fun setCompletionListener(listener: () -> Unit) {
        completionListener = listener
    }

    override fun play() {
        mediaPlayer.start()
        changeStateTo(PlayerState.PLAYING)
    }

    override fun pause() {
        mediaPlayer.pause()
        changeStateTo(PlayerState.PAUSED)
    }

    override fun release() {
        mediaPlayer.release()
    }

    private fun changeStateTo(state: PlayerState) {
        this.state = state
        stateListener?.stateChanged(state)
    }
}