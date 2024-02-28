package com.example.playlistmaker.library.domain.playlists

import com.example.playlistmaker.library.domain.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {

    suspend fun getPlaylists(): Flow<List<Playlist>>
    suspend fun addTrackToPlaylist(playlist: Playlist, trackId: String)
}