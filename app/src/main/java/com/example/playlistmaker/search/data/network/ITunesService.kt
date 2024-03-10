package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class ITunesService(
    private val service: ITunesApi
) {
    suspend fun search(
        text: String
    ): List<Track> {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.search(text)
                return@withContext response.tracks.mapNotNull<TrackDto, Track> { map(it) }
            } catch (e: Throwable) {
                throw e
            }
        }
    }

    private fun map(dto: TrackDto): Track? {
        if (dto.trackId != null && dto.trackName != null && dto.artistName != null) {
            return Track(
                trackId = dto.trackId,
                trackName = dto.trackName,
                artistName = dto.artistName,
                artworkUrl100 = dto.artworkUrl100,
                country = dto.country ?: "",
                trackTimeMillis = dto.trackTimeMillis ?: 0,
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
