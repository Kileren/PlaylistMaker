package com.example.playlistmaker.search.domain

interface SearchHistoryStorage {
    var searchHistory: Array<Track>
    fun addTrackToHistory(track: Track)
    fun getTrack(id: String): Track
}