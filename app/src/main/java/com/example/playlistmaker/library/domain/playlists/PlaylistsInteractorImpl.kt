package com.example.playlistmaker.library.domain.playlists

import com.example.playlistmaker.library.domain.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(
    private val repository: PlaylistsRepository
): PlaylistsInteractor {
    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return repository.getPlaylists()
    }

    override suspend fun addTrackToPlaylist(playlist: Playlist, trackId: String) {
        val newPlaylist = playlist.copy(
            tracks = playlist.tracks + trackId,
            numberOfTracks = playlist.numberOfTracks + 1
        )
        repository.createPlaylist(newPlaylist)
    }
}