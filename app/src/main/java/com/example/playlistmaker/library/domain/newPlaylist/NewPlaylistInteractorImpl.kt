package com.example.playlistmaker.library.domain.newPlaylist

import android.net.Uri
import com.example.playlistmaker.library.domain.Playlist
import com.example.playlistmaker.library.domain.playlists.PlaylistsRepository

class NewPlaylistInteractorImpl(
    private val repository: PlaylistsRepository
): NewPlaylistInteractor {

    override fun saveCoverImage(uri: Uri, imageName: String) {
        repository.saveCoverImage(uri, imageName)
    }

    override fun getCoverImage(playlistTitle: String): Uri? {
        return repository.getCoverImage(playlistTitle)
    }

    override suspend fun createPlaylist(playlist: Playlist) {
        repository.createPlaylist(playlist)
    }
}