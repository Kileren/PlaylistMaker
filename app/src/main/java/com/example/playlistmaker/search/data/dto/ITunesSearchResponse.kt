package com.example.playlistmaker.search.data.dto

import com.google.gson.annotations.SerializedName

data class ITunesSearchResponse(
    @SerializedName("results") val tracks: List<TrackDto>
)