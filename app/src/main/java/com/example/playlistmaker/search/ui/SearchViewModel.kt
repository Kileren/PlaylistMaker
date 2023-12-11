package com.example.playlistmaker.search.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.search.domain.SearchInteractor

class SearchViewModel(
    private val interactor: SearchInteractor
): ViewModel() {

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { search() }

    // State

    private val state = MutableLiveData<SearchState>()
    fun getState(): LiveData<SearchState> = state

    private val searchFieldText = MutableLiveData<String>()
    fun getSearchFieldText(): LiveData<String> = searchFieldText

    private var currentSearchText: String = ""

    // Public

    fun onSaveInstanceState(outState: Bundle, searchText: String) {
        outState.putString(SEARCH_VALUE, searchText)
    }

    fun onRestoreInstanceState(savedInstanceState: Bundle) {
        val searchText = savedInstanceState.getString(SEARCH_VALUE)
        if (searchText != null) {
            searchFieldText.postValue(searchText)
        }
    }

    fun onSearchTextChanged(text: String?, hasFocus: Boolean) {
        currentSearchText = text ?: ""
        if (hasFocus && text?.isEmpty() == true) {
            showHistory()
        } else {
            state.postValue(null)
        }
        searchDebounce()
    }

    fun onFocusChanged(text: String, hasFocus: Boolean) {
        if (hasFocus && text.isEmpty()) {
            showHistory()
        } else {
            state.postValue(null)
        }
    }

    fun tapOnClearHistoryButton() {
        interactor.clearSearchHistory()
        state.postValue(null)
    }

    fun tapOnClearTextButton() {
        showHistory()
        searchFieldText.postValue("")
    }

    fun tapOnSearchButton() {
        search()
    }

    fun tapRefreshButton(text: String) {
        search(text)
    }

    fun tapOnTrack(track: Track) {
        interactor.addTrackToSearchHistory(track)
        if (state.value is SearchState.History) {
            showHistory()
        }
    }

    // Private

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun search(text: String? = null) {
        val textToSearch = text ?: currentSearchText
        if (textToSearch.isEmpty()) return

        state.postValue(SearchState.Loading)
        interactor.search(
            text = textToSearch,
            onSuccess = {
                state.postValue(SearchState.Result(it))
            },
            onError = {
                state.postValue(SearchState.Error(textToSearch))
            }
        )
    }

    private fun showHistory() {
        val tracks = interactor.getSearchHistory()
        state.postValue(SearchState.History(tracks))
    }

    // Helpers

    companion object {
        private const val SEARCH_VALUE = "SEARCH_VALUE"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L

        fun getViewModelFactory(
            searchInteractor: SearchInteractor,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(searchInteractor)
            }
        }
    }
}