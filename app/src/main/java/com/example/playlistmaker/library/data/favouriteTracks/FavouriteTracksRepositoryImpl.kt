package com.example.playlistmaker.library.data.favouriteTracks

import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.library.data.db.TrackDbConverter
import com.example.playlistmaker.library.domain.favouriteTracks.FavouriteTracksRepository
import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavouriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private var trackDbConverter: TrackDbConverter
): FavouriteTracksRepository {
    override suspend fun addTrack(track: Track) {
        val trackEntity = trackDbConverter.map(track)
        appDatabase.favouriteTracksDao().addTrack(trackEntity)
    }

    override suspend fun removeTrack(track: Track) {
        val trackEntity = trackDbConverter.map(track)
        appDatabase.favouriteTracksDao().removeTrack(trackEntity)
    }

    override fun getTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.favouriteTracksDao()
            .getTracks()
            .map { trackDbConverter.map(it) }
        emit(tracks)
    }
}