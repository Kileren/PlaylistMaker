package com.example.playlistmaker.library.domain.playlists

import android.net.Uri
import com.example.playlistmaker.library.domain.Playlist
import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    suspend fun getPlaylists(): Flow<List<Playlist>>
    suspend fun getPlaylist(id: Int): Flow<Playlist?>
    suspend fun createPlaylist(playlist: Playlist)
    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track)
    suspend fun getTracksInPlaylist(playlist: Playlist): Flow<List<Track>>
    suspend fun getTrackSavedInPlaylist(trackId: String): Track
    fun saveCoverImage(uri: Uri, imageName: String)
    fun getCoverImage(playlistTitle: String): Uri?
}