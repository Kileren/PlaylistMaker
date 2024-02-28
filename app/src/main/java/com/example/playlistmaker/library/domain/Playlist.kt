package com.example.playlistmaker.library.domain

import android.net.Uri

data class Playlist(
    val id: Int,
    val title: String,
    val description: String?,
    val coverUri: Uri?,
    val numberOfTracks: Int
)