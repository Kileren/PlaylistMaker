package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.data.Endpoint
import com.example.playlistmaker.data.dto.ITunesSearchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ITunesService {

    private val converterFactory by lazy { GsonConverterFactory.create() }
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Endpoint.ITUNES.baseUrl)
            .addConverterFactory(converterFactory)
            .build()
    }
    private val service by lazy { retrofit.create(ITunesApi::class.java) }

    fun search(
        text: String,
        onSuccess: (List<TrackDto>) -> Unit,
        onError: () -> Unit
    ) {
        service.search(text).enqueue(object : Callback<ITunesSearchResponse> {
            override fun onResponse(
                call: Call<ITunesSearchResponse>,
                response: Response<ITunesSearchResponse>
            ) {
                when (response.code()) {
                    200 -> {
                        val songs = response.body()?.tracks
                        onSuccess(songs ?: listOf())
                    }
                    else -> {
                        onError()
                    }
                }
            }

            override fun onFailure(call: Call<ITunesSearchResponse>, t: Throwable) {
                onError()
            }
        })
    }
}
