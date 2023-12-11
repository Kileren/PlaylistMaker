package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.data.PlayerImpl
import com.example.playlistmaker.data.network.ITunesService
import com.example.playlistmaker.search.data.SearchHistoryStorageImpl
import com.example.playlistmaker.data.storage.SharedPreferencesStorage
import com.example.playlistmaker.domain.api.AudioPlayerInteractor
import com.example.playlistmaker.domain.api.Player
import com.example.playlistmaker.search.domain.SearchHistoryStorage
import com.example.playlistmaker.domain.impl.AudioPlayerInteractorImpl
import com.example.playlistmaker.presentation.api.AudioPlayer
import com.example.playlistmaker.presentation.api.AudioPlayerPresenter
import com.example.playlistmaker.presentation.presenters.AudioPlayerPresenterImpl
import com.example.playlistmaker.search.data.SearchRepositoryImpl
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.domain.SearchInteractorImpl
import com.example.playlistmaker.search.domain.SearchRepository
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsInteractorImpl
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.SharingInteractorImpl

object Creator {

    fun createAudioPlayerPresenter(context: Context, audioPlayer: AudioPlayer): AudioPlayerPresenter {
        return AudioPlayerPresenterImpl(
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

    private fun createPlayer(): Player = PlayerImpl()

    private fun createSearchHistoryStorage(context: Context): SearchHistoryStorage {
        return SearchHistoryStorageImpl(context)
    }

    // Shared

    private fun createITunesService(): ITunesService {
        return ITunesService()
    }

    private fun createSharedPreferencesStorage(context: Context): SharedPreferencesStorage {
        return SharedPreferencesStorage(context)
    }

    private fun createExternalNavigator(context: Context): ExternalNavigator {
        return ExternalNavigatorImpl(context)
    }

    fun createSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(
            externalNavigator = createExternalNavigator(context)
        )
    }

    // Search

    private fun createSearchRepository(): SearchRepository {
        return SearchRepositoryImpl(
            iTunesService = createITunesService()
        )
    }

    fun createSearchInteractor(context: Context): SearchInteractor {
        return SearchInteractorImpl(
            searchRepository = createSearchRepository(),
            searchHistoryStorage = createSearchHistoryStorage(context)
        )
    }

    // Settings

    private fun createSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(
            prefs = createSharedPreferencesStorage(context)
        )
    }

    fun createSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(
            repository = createSettingsRepository(context)
        )
    }
}