package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.impl.PlayerState
import com.example.playlistmaker.domain.models.Track

interface AudioPlayerInteractor {

    val currentPlayerState: PlayerState
    val currentPlayerPosition: Int

    fun loadTrack(id: String, consumer: AudioPlayerConsumer)
    fun setPlayerStateListener(listener: Player.StateListener)
    fun setPlayerCompletionListener(listener: () -> Unit)

    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()

    interface AudioPlayerConsumer {
        fun consume(track: Track)
    }
}