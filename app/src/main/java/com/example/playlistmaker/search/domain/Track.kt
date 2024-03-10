package com.example.playlistmaker.search.domain

import java.text.SimpleDateFormat
import java.util.Locale

data class Track(
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val artworkUrl100: String?,
    val country: String,
    val trackTimeMillis: Long,
    val releaseDate: String?,
    val previewUrl: String?,
    val genreName: String,
    val albumName: String?
) {
    var isFavourite: Boolean = false
    val trackTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
}