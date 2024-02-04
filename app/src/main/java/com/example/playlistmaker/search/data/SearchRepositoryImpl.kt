package com.example.playlistmaker.search.data

import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.search.domain.Track
import com.example.playlistmaker.search.data.network.ITunesService
import com.example.playlistmaker.search.domain.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepositoryImpl(
    private val iTunesService: ITunesService,
    private val appDatabase: AppDatabase
): SearchRepository {
    override fun search(text: String): Flow<List<Track>> = flow {
        val tracks = iTunesService.search(text)
        val favouriteTracksIDs = appDatabase.favouriteTracksDao().getTracksIDs()
        tracks.forEach {
            it.isFavourite = favouriteTracksIDs.contains(it.trackId)
        }
        emit(tracks)
    }
}