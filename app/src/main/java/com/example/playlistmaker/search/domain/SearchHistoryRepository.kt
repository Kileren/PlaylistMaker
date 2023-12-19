package com.example.playlistmaker.search.domain

interface SearchHistoryRepository {
    var searchHistory: Array<Track>
    fun addTrackToHistory(track: Track)
    fun getTrack(id: String): Track
}