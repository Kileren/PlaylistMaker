package com.example.playlistmaker.library.domain.newPlaylist

import android.net.Uri
import com.example.playlistmaker.library.domain.Playlist

interface NewPlaylistInteractor {
    fun saveCoverImage(uri: Uri, imageName: String)
    fun getCoverImage(playlistTitle: String): Uri?
    suspend fun createPlaylist(playlist: Playlist)
}