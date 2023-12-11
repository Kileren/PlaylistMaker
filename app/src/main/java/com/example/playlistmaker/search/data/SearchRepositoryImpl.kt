package com.example.playlistmaker.search.data

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.search.data.network.ITunesService
import com.example.playlistmaker.search.domain.SearchRepository

class SearchRepositoryImpl(
    private val iTunesService: ITunesService
): SearchRepository {
    override fun search(text: String, onSuccess: (List<Track>) -> Unit, onError: () -> Unit) {
        iTunesService.search(
            text = text,
            onSuccess = onSuccess,
            onError = onError
        )
    }
}