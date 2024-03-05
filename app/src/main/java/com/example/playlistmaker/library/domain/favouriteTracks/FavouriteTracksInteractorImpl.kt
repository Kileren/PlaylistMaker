package com.example.playlistmaker.library.domain.favouriteTracks

import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavouriteTracksInteractorImpl(
    private val repository: FavouriteTracksRepository
): FavouriteTracksInteractor {
    override suspend fun addTrack(track: Track) {
        repository.addTrack(track)
    }

    override suspend fun removeTrack(track: Track) {
        repository.removeTrack(track)
    }

    override fun getTracks(): Flow<List<Track>> {
        return repository.getTracks().map { it.reversed() }
    }
}