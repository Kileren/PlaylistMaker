package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.AudioPlayerInteractor
import com.example.playlistmaker.domain.api.Player
import com.example.playlistmaker.domain.api.SearchHistoryStorage

class AudioPlayerInteractorImpl(
    val player: Player,
    val searchHistoryStorage: SearchHistoryStorage
): AudioPlayerInteractor {
    override val currentPlayerState: PlayerState
        get() = player.state
    override val currentPlayerPosition: Int
        get() = player.currentPosition

    override fun loadTrack(
        id: String,
        consumer: AudioPlayerInteractor.AudioPlayerConsumer
    ) {
        val track = searchHistoryStorage.getTrack(id)

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