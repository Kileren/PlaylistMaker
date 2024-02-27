package com.example.playlistmaker.library.domain

data class Playlist(
    val id: Int,
    val title: String,
    val description: String?,
    val coverUri: String?
)