package com.example.playlistmaker.library.domain

import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface FavouriteTracksRepository {
    suspend fun addTrack(track: Track)
    suspend fun removeTrack(track: Track)
    fun getTracks(): Flow<List<Track>>
}