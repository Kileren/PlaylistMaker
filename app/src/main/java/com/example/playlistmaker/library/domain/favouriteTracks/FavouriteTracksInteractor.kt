package com.example.playlistmaker.library.domain.favouriteTracks

import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface FavouriteTracksInteractor {
    suspend fun addTrack(track: Track)
    suspend fun removeTrack(track: Track)
    fun getTracks(): Flow<List<Track>>
}