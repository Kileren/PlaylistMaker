package com.example.playlistmaker.library.ui.playlists

import com.example.playlistmaker.library.domain.Playlist

sealed interface PlaylistsState {

    object Empty: PlaylistsState

    data class Content(
        val playlists: List<Playlist>
    ): PlaylistsState
}