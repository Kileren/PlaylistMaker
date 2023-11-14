package com.example.playlistmaker

import android.content.Context
import com.example.playlistmaker.data.PlayerImpl
import com.example.playlistmaker.data.storage.SearchHistoryStorageImpl
import com.example.playlistmaker.domain.api.AudioPlayerInteractor
import com.example.playlistmaker.domain.api.Player
import com.example.playlistmaker.domain.api.SearchHistoryStorage
import com.example.playlistmaker.domain.impl.AudioPlayerInteractorImpl
import com.example.playlistmaker.presentation.api.AudioPlayer
import com.example.playlistmaker.presentation.presenters.AudioPlayerPresenter

object Creator {

    fun createAudioPlayerPresenter(context: Context, audioPlayer: AudioPlayer): AudioPlayerPresenter {
        return AudioPlayerPresenter(
            audioPlayer = audioPlayer,
            audioPlayerInteractor = createAudioPlayerInteractor(context)
        )
    }

    private fun createAudioPlayerInteractor(context: Context): AudioPlayerInteractor {
        return AudioPlayerInteractorImpl(
            player = createPlayer(),
            searchHistoryStorage = createSearchHistoryStorage(context)
        )
    }

    private fun createPlayer(): Player {
        return PlayerImpl()
    }

    private fun createSearchHistoryStorage(context: Context): SearchHistoryStorage {
        return SearchHistoryStorageImpl(context)
    }
}