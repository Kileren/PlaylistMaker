package com.example.playlistmaker.player.ui.models

data class TrackInfo(
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val artworkUrl100: String?,
    val coverArtwork: String?,
    val country: String,
    val trackTime: String,
    val genreName: String,
    val albumName: String?,
    val releaseYear: String?
)