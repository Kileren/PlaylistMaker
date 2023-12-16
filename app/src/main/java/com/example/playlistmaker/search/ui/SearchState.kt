package com.example.playlistmaker.search.ui

import com.example.playlistmaker.search.domain.Track

sealed class SearchState {

    object Loading: SearchState()

    class Error(
        val text: String
    ): SearchState()

    class History(
        val tracks: Array<Track>
    ): SearchState()

    class Result(
        val tracks: List<Track>
    ): SearchState()
}