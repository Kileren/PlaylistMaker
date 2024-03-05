package com.example.playlistmaker.di

import com.example.playlistmaker.library.ui.liked.LikedSongsViewModel
import com.example.playlistmaker.library.ui.newPlaylist.NewPlaylistViewModel
import com.example.playlistmaker.library.ui.playlists.PlaylistsViewModel
import com.example.playlistmaker.player.ui.AudioPlayerViewModel
import com.example.playlistmaker.search.ui.SearchViewModel
import com.example.playlistmaker.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        AudioPlayerViewModel(get(), get(), get())
    }

    viewModel {
        SearchViewModel(get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel {
        LikedSongsViewModel(get())
    }

    viewModel {
        PlaylistsViewModel(get())
    }

    viewModel {
        NewPlaylistViewModel(get())
    }
}