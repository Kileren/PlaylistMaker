package com.example.playlistmaker.library.data.newPlaylist

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.library.data.db.entities.PlaylistEntity
import com.example.playlistmaker.library.domain.Playlist
import com.example.playlistmaker.library.domain.newPlaylist.NewPlaylistRepository
import java.io.File
import java.io.FileOutputStream

class NewPlaylistRepositoryImpl(
    private val context: Context,
    private val appDatabase: AppDatabase
): NewPlaylistRepository {

    override fun saveCoverImage(uri: Uri, imageName: String) {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), ALBUM_NAME)
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "$imageName.jpg")
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    override suspend fun createPlaylist(playlist: Playlist) {
        appDatabase.playlistsDao().addPlaylist(map(playlist))
    }

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            id = playlist.id,
            title = playlist.title,
            description = playlist.description,
            coverUri = playlist.coverUri
        )
    }

    companion object {
        private const val ALBUM_NAME = "PlaylistsAlbum"
    }
}