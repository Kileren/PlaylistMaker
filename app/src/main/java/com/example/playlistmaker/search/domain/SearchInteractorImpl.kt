package com.example.playlistmaker.search.domain

import kotlinx.coroutines.flow.Flow

class SearchInteractorImpl(
    private val searchRepository: SearchRepository,
    private val searchHistoryRepository: SearchHistoryRepository
): SearchInteractor {

    override suspend fun getSearchHistory(): List<Track> = searchHistoryRepository.getSearchHistory()

    override suspend fun clearSearchHistory() {
        searchHistoryRepository.setSearchHistory(listOf())
    }

    override suspend fun addTrackToSearchHistory(track: Track) {
        searchHistoryRepository.addTrackToHistory(track)
    }

    override suspend fun search(text: String): Flow<List<Track>> {
        return searchRepository.search(text)
    }
}