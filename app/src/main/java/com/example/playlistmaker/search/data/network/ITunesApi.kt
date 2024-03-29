package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.dto.ITunesSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {

    @GET("/search")
    suspend fun search(@Query("term") text: String): ITunesSearchResponse
}