package com.example.playlistmaker.library.domain.newPlaylist

import android.net.Uri
import com.example.playlistmaker.library.domain.Playlist

class NewPlaylistInteractorImpl(
    private val repository: NewPlaylistRepository
): NewPlaylistInteractor {

    override fun saveCoverImage(uri: Uri, imageName: String) {
        repository.saveCoverImage(uri, imageName)
    }

    override suspend fun createPlaylist(playlist: Playlist) {
        repository.createPlaylist(playlist)
    }
}