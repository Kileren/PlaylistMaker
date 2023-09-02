package com.example.playlistmaker.network.responses

import com.google.gson.annotations.SerializedName

data class ITunesSearchResponse(
    @SerializedName("results") val songs: List<Song>
)

data class Song(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
)