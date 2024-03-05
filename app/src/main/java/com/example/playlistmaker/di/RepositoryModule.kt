package com.example.playlistmaker.di

import com.example.playlistmaker.library.data.favouriteTracks.FavouriteTracksRepositoryImpl
import com.example.playlistmaker.library.data.playlists.PlaylistsRepositoryImpl
import com.example.playlistmaker.library.domain.favouriteTracks.FavouriteTracksRepository
import com.example.playlistmaker.library.domain.playlists.PlaylistsRepository
import com.example.playlistmaker.search.data.SearchHistoryRepositoryImpl
import com.example.playlistmaker.search.data.SearchRepositoryImpl
import com.example.playlistmaker.search.domain.SearchHistoryRepository
import com.example.playlistmaker.search.domain.SearchRepository
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.SettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    factory<SearchRepository> {
        SearchRepositoryImpl(get(), get())
    }

    factory<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(androidContext(), get())
    }

    single<FavouriteTracksRepository> {
        FavouriteTracksRepositoryImpl(get(), get())
    }

    single<PlaylistsRepository> {
        PlaylistsRepositoryImpl(androidContext(), get(), get())
    }
}