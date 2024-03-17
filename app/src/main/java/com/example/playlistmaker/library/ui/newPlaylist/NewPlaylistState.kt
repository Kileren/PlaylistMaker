package com.example.playlistmaker.library.ui.newPlaylist

sealed interface NewPlaylistState {

    object Close: NewPlaylistState

    data class Edit(
        val useImagePlaceholder: Boolean,
        val title: String?,
        val description: String?
    ): NewPlaylistState
}