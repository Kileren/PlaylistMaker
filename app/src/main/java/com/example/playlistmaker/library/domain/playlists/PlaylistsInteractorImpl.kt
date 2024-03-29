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

    override suspend fun getPlaylist(id: Int): Flow<Playlist?> {
        return repository.getPlaylist(id)
    }

    override suspend fun deletePlaylist(id: Int) {
        repository.deletePlaylist(id)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        repository.updatePlaylist(playlist)
    }

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        repository.addTrackToPlaylist(playlist, track)
    }

    override suspend fun getTracksInPlaylist(playlist: Playlist): Flow<List<Track>> {
        return repository.getTracksInPlaylist(playlist)
    }

    override suspend fun removeTrackFromPlaylist(playlistId: Int, trackId: String) {
        repository.removeTrackFromPlaylist(playlistId, trackId)
    }
}