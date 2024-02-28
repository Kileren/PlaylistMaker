package com.example.playlistmaker.search.ui

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.Track
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.utils.debounce
import kotlinx.coroutines.launch

class SearchViewModel(
    private val interactor: SearchInteractor
): ViewModel() {

    private lateinit var searchDebounce: (String) -> Unit

    // State

    private val _state = MutableLiveData<SearchState>()
    val state: LiveData<SearchState> = _state

    private val _searchFieldText = MutableLiveData<String>()
    val searchFieldText: LiveData<String> = _searchFieldText

    private var currentSearchText: String = ""

    // Public

    fun onViewCreated() {
        searchDebounce = debounce<String>(
            SEARCH_DEBOUNCE_DELAY,
            viewModelScope,
            true
        ) {
            search()
        }
    }

    fun onSaveInstanceState(outState: Bundle, searchText: String) {
        outState.putString(SEARCH_VALUE, searchText)
    }

    fun onRestoreInstanceState(savedInstanceState: Bundle) {
        val searchText = savedInstanceState.getString(SEARCH_VALUE)
        if (searchText != null) {
            _searchFieldText.postValue(searchText)
        }
    }

    fun onSearchTextChanged(text: String?, hasFocus: Boolean) {
        currentSearchText = text ?: ""
        if (hasFocus && text?.isEmpty() == true) {
            showHistory()
        } else {
            _state.postValue(null)
        }
        searchDebounce(text ?: "")
    }

    fun onFocusChanged(text: String, hasFocus: Boolean) {
        if (hasFocus && text.isEmpty()) {
            showHistory()
        } else {
            _state.postValue(null)
        }
    }

    fun tapOnClearHistoryButton() {
        viewModelScope.launch {
            interactor.clearSearchHistory()
            _state.postValue(null)
        }
    }

    fun tapOnClearTextButton() {
        showHistory()
        _searchFieldText.postValue("")
    }

    fun tapOnSearchButton() {
        search()
    }

    fun tapRefreshButton(text: String) {
        search(text)
    }

    fun tapOnTrack(track: Track, completion: () -> Unit) {
        viewModelScope.launch {
            interactor.addTrackToSearchHistory(track)
            if (state.value is SearchState.History) {
                showHistory()
            }
            completion()
        }
    }

    // Private

    private fun search(text: String? = null) {
        val textToSearch = text ?: currentSearchText
        if (textToSearch.isEmpty()) return

        _state.postValue(SearchState.Loading)
        viewModelScope.launch {
            try {
                interactor
                    .search(textToSearch)
                    .collect {
                        _state.postValue(SearchState.Result(it))
                    }
            } catch (e: Throwable) {
                _state.postValue((SearchState.Error(textToSearch)))
            }
        }
    }

    private fun showHistory() {
        viewModelScope.launch {
            val tracks = interactor.getSearchHistory()
            _state.postValue(SearchState.History(tracks))
        }
    }

    // Helpers

    companion object {
        private const val SEARCH_VALUE = "SEARCH_VALUE"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}