package com.example.playlistmaker.search.domain

data class Track(
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val artworkUrl100: String?,
    val country: String,
    val trackTime: String,
    val releaseDate: String?,
    val previewUrl: String?,
    val genreName: String,
    val albumName: String?
) {
    var isFavourite: Boolean = false
}