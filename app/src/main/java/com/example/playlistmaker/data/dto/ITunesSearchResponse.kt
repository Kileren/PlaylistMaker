package com.example.playlistmaker.data.dto

import com.google.gson.annotations.SerializedName

data class ITunesSearchResponse(
    @SerializedName("results") val tracks: List<TrackDto>
)