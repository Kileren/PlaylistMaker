package com.example.playlistmaker.library.domain.playlists

import com.example.playlistmaker.library.domain.Playlist
import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(
    private val repository: PlaylistsRepository
): PlaylistsInteractor {
    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return repository.getPlaylists()
    }

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        repository.addTrackToPlaylist(playlist, track)
    }
}