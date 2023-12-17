package com.example.playlistmaker.search.domain

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