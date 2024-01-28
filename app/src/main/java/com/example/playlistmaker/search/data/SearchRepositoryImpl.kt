package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.domain.Track
import com.example.playlistmaker.search.data.network.ITunesService
import com.example.playlistmaker.search.domain.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepositoryImpl(
    private val iTunesService: ITunesService
): SearchRepository {
    override fun search(text: String): Flow<List<Track>> = flow {
        val tracks = iTunesService.search(text)
        emit(tracks)
    }
}