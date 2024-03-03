package com.example.playlistmaker.library.domain.playlists

import android.net.Uri
import com.example.playlistmaker.library.domain.Playlist
import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    suspend fun getPlaylists(): Flow<List<Playlist>>
    suspend fun createPlaylist(playlist: Playlist)
    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track)
    fun saveCoverImage(uri: Uri, imageName: String)
    fun getCoverImage(playlistTitle: String): Uri?
}