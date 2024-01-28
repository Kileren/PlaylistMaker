package com.example.playlistmaker.search.domain

import kotlinx.coroutines.flow.Flow

class SearchInteractorImpl(
    private val searchRepository: SearchRepository,
    private val searchHistoryRepository: SearchHistoryRepository
): SearchInteractor {

    override fun getSearchHistory(): Array<Track> = searchHistoryRepository.searchHistory

    override fun clearSearchHistory() {
        searchHistoryRepository.searchHistory = arrayOf()
    }

    override fun addTrackToSearchHistory(track: Track) {
        searchHistoryRepository.addTrackToHistory(track)
    }

    override suspend fun search(text: String): Flow<List<Track>> {
        return searchRepository.search(text)
    }
}