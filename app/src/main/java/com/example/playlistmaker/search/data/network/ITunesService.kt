package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.data.dto.ITunesSearchResponse
import com.example.playlistmaker.search.domain.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Locale

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
        onSuccess: (List<Track>) -> Unit,
        onError: () -> Unit
    ) {
        service.search(text).enqueue(object : Callback<ITunesSearchResponse> {
            override fun onResponse(
                call: Call<ITunesSearchResponse>,
                response: Response<ITunesSearchResponse>
            ) {
                when (response.code()) {
                    200 -> {
                        val songs = response.body()?.tracks?.mapNotNull { map(it) }
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

    private fun map(dto: TrackDto): Track? {
        if (dto.trackId != null && dto.trackName != null && dto.artistName != null) {
            return Track(
                trackId = dto.trackId,
                trackName = dto.trackName,
                artistName = dto.artistName,
                artworkUrl100 = dto.artworkUrl100,
                country = dto.country ?: "",
                trackTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(dto.trackTimeMillis),
                releaseDate = dto.releaseDate,
                previewUrl = dto.previewUrl,
                genreName = dto.genreName ?: "",
                albumName = dto.albumName
            )
        } else {
            return null
        }
    }
}
