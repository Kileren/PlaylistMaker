package com.example.playlistmaker.player.ui.models

sealed interface AddingToPlaylistResult {
    class Added(val playlistName: String): AddingToPlaylistResult
    class AlreadyExists(val playlistName: String): AddingToPlaylistResult
}