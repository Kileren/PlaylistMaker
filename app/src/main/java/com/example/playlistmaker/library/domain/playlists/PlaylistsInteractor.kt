package com.example.playlistmaker.library.domain.playlists

import com.example.playlistmaker.library.domain.Playlist
import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {

    suspend fun getPlaylists(): Flow<List<Playlist>>
    suspend fun getPlaylist(id: Int): Flow<Playlist?>
    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track)
    suspend fun getTracksInPlaylist(playlist: Playlist): Flow<List<Track>>
    suspend fun removeTrackFromPlaylist(playlistId: Int, trackId: String)
}