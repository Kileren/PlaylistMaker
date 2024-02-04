package com.example.playlistmaker.search.domain

interface SearchHistoryRepository {
    suspend fun getSearchHistory(): List<Track>
    suspend fun setSearchHistory(history: List<Track>)
    suspend fun addTrackToHistory(track: Track)
    suspend fun getTrack(id: String): Track
}