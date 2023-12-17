package com.example.playlistmaker.search.domain

interface SearchRepository {
    fun search(
        text: String,
        onSuccess: (List<Track>) -> Unit,
        onError: () -> Unit
    )
}