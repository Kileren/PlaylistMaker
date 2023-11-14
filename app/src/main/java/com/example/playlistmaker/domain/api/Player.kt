package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.impl.PlayerState

interface Player {
    var state: PlayerState
    val currentPosition: Int
    fun setUpPlayer(previewURL: String)
    fun setStateListener(listener: StateListener)
    fun setCompletionListener(listener: () -> Unit)
    fun play()
    fun pause()
    fun release()

    interface StateListener {
        fun stateChanged(state: PlayerState)
    }
}