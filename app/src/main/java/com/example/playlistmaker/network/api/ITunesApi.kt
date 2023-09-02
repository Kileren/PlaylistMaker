package com.example.playlistmaker.network.api

import com.example.playlistmaker.network.responses.ITunesSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {

    @GET("/search")
    fun search(@Query("term") text: String): Call<ITunesSearchResponse>
}