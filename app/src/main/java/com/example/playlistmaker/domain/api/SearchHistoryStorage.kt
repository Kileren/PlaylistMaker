package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface SearchHistoryStorage {
    var searchHistory: Array<Track>
    fun addTrackToHistory(track: Track)
    fun getTrack(id: String): Track
}