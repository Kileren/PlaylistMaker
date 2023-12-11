package com.example.playlistmaker.search.domain

import com.example.playlistmaker.domain.models.Track

interface SearchRepository {
    fun search(
        text: String,
        onSuccess: (List<Track>) -> Unit,
        onError: () -> Unit
    )
}