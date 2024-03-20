package com.example.playlistmaker.di

import com.example.playlistmaker.library.domain.favouriteTracks.FavouriteTracksInteractor
import com.example.playlistmaker.library.domain.favouriteTracks.FavouriteTracksInteractorImpl
import com.example.playlistmaker.library.domain.newPlaylist.NewPlaylistInteractor
import com.example.playlistmaker.library.domain.newPlaylist.NewPlaylistInteractorImpl
import com.example.playlistmaker.library.domain.playlists.PlaylistsInteractor
import com.example.playlistmaker.library.domain.playlists.PlaylistsInteractorImpl
import com.example.playlistmaker.player.domain.api.AudioPlayerInteractor
import com.example.playlistmaker.player.domain.impl.AudioPlayerInteractorImpl
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.domain.SearchInteractorImpl
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsInteractorImpl
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    factory<AudioPlayerInteractor> {
        AudioPlayerInteractorImpl(get(), get(), get())
    }

    factory<SearchInteractor> {
        SearchInteractorImpl(get(), get())
    }

    factory<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    factory<SharingInteractor> {
        SharingInteractorImpl(get())
    }

    factory<FavouriteTracksInteractor> {
        FavouriteTracksInteractorImpl(get())
    }

    factory<NewPlaylistInteractor> {
        NewPlaylistInteractorImpl(get())
    }

    factory<PlaylistsInteractor> {
        PlaylistsInteractorImpl(get())
    }
}