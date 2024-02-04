package com.example.playlistmaker.search.domain

import kotlinx.coroutines.flow.Flow

interface SearchInteractor {
    suspend fun getSearchHistory(): List<Track>
    suspend fun clearSearchHistory()
    suspend fun addTrackToSearchHistory(track: Track)

    suspend fun search(
        text: String,
    ): Flow<List<Track>>
}