package com.example.playlistmaker.library.ui.playlist

import android.net.Uri
import com.example.playlistmaker.search.domain.Track

sealed interface PlaylistState {

    object Removed: PlaylistState

    data class Content(
        val coverUri: Uri?,
        val title: String,
        val description: String?,
        val totalDuration: String,
        val totalTracks: String,
        val tracks: List<Track>
    ): PlaylistState
}