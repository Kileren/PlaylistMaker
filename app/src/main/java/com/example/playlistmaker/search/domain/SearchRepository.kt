package com.example.playlistmaker.search.domain

import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun search(
        text: String
    ): Flow<List<Track>>
}