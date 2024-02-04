package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.example.playlistmaker.player.domain.api.Player
import com.example.playlistmaker.search.domain.SearchHistoryRepository
import com.example.playlistmaker.search.domain.Track

class AudioPlayerInteractorImpl(
    val player: Player,
    val searchHistoryRepository: SearchHistoryRepository
): AudioPlayerInteractor {
    override val currentPlayerState: PlayerState
        get() = player.state
    override val currentPlayerPosition: Int
        get() = player.currentPosition

    override suspend fun getTrack(id: String): Track {
        return searchHistoryRepository.getTrack(id)
    }

    override suspend fun loadTrack(
        id: String,
        consumer: AudioPlayerInteractor.AudioPlayerConsumer
    ) {
        val track = getTrack(id)
        if (track.previewUrl != null) {
            player.setUpPlayer(track.previewUrl)
        }
        consumer.consume(track)
    }

    override fun setPlayerStateListener(listener: Player.StateListener) {
        player.setStateListener(listener)
    }

    override fun setPlayerCompletionListener(listener: () -> Unit) {
        player.setCompletionListener(listener)
    }

    override fun startPlayer() = player.play()
    override fun pausePlayer() = player.pause()
    override fun releasePlayer() = player.release()
}