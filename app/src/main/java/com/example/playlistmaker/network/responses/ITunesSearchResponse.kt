package com.example.playlistmaker.network.responses

import com.example.playlistmaker.models.Track
import com.google.gson.annotations.SerializedName

data class ITunesSearchResponse(
    @SerializedName("results") val tracks: List<Track>
)