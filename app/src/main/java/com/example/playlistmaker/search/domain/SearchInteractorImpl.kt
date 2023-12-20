package com.example.playlistmaker.search.domain

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

    override fun search(text: String, onSuccess: (List<Track>) -> Unit, onError: () -> Unit) {
        searchRepository.search(text, onSuccess, onError)
    }
}