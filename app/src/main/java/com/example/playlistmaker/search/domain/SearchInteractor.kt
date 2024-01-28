package com.example.playlistmaker.search.domain

import kotlinx.coroutines.flow.Flow

interface SearchInteractor {
    fun getSearchHistory(): Array<Track>
    fun clearSearchHistory()
    fun addTrackToSearchHistory(track: Track)

    suspend fun search(
        text: String,
    ): Flow<List<Track>>
}