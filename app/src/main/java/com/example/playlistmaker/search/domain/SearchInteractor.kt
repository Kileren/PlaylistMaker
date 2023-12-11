package com.example.playlistmaker.search.domain

import com.example.playlistmaker.domain.models.Track

interface SearchInteractor {
    fun getSearchHistory(): Array<Track>
    fun clearSearchHistory()
    fun addTrackToSearchHistory(track: Track)

    fun search(
        text: String,
        onSuccess: (List<Track>) -> Unit,
        onError: () -> Unit
    )
}