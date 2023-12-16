package com.example.playlistmaker.search.domain

class SearchInteractorImpl(
    private val searchRepository: SearchRepository,
    private val searchHistoryStorage: SearchHistoryStorage
): SearchInteractor {

    override fun getSearchHistory(): Array<Track> = searchHistoryStorage.searchHistory

    override fun clearSearchHistory() {
        searchHistoryStorage.searchHistory = arrayOf()
    }

    override fun addTrackToSearchHistory(track: Track) {
        searchHistoryStorage.addTrackToHistory(track)
    }

    override fun search(text: String, onSuccess: (List<Track>) -> Unit, onError: () -> Unit) {
        searchRepository.search(text, onSuccess, onError)
    }
}