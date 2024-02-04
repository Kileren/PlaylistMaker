package com.example.playlistmaker.player.domain.api

import com.example.playlistmaker.player.domain.impl.PlayerState
import com.example.playlistmaker.search.domain.Track

interface AudioPlayerInteractor {

    val currentPlayerState: PlayerState
    val currentPlayerPosition: Int

    suspend fun getTrack(id: String): Track

    suspend fun loadTrack(id: String, consumer: AudioPlayerConsumer)
    fun setPlayerStateListener(listener: Player.StateListener)
    fun setPlayerCompletionListener(listener: () -> Unit)

    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()

    interface AudioPlayerConsumer {
        fun consume(track: Track)
    }
}