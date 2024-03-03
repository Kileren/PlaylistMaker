package com.example.playlistmaker.library.data.playlists

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.library.data.db.TrackDbConverter
import com.example.playlistmaker.library.data.db.entities.PlaylistEntity
import com.example.playlistmaker.library.domain.Playlist
import com.example.playlistmaker.library.domain.playlists.PlaylistsRepository
import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileOutputStream

class PlaylistsRepositoryImpl(
    private val context: Context,
    private val appDatabase: AppDatabase,
    private val trackDbConverter: TrackDbConverter
): PlaylistsRepository {

    override suspend fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistsDao().getPlaylists().map { map(it) }
        emit(playlists)
    }

    override suspend fun createPlaylist(playlist: Playlist) {
        appDatabase.playlistsDao().addPlaylist(map(playlist))
    }

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        val newPlaylist = playlist.copy(
            tracks = playlist.tracks + track.trackId,
            numberOfTracks = playlist.numberOfTracks + 1
        )
        appDatabase.playlistsDao().addPlaylist(map(newPlaylist))
        appDatabase.playlistTracksDao().addTrack(trackDbConverter.mapForPlaylist(track))
    }

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

    override fun getCoverImage(playlistTitle: String): Uri? {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), ALBUM_NAME)
        val file = File(filePath, "$playlistTitle.jpg")
        return if (file.exists()) {
            file.toUri()
        } else {
            null
        }
    }

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            id = playlist.id,
            title = playlist.title,
            description = playlist.description,
            coverUri = playlist.coverUri?.toString(),
            tracks = playlist.tracks.joinToString(separator = ","),
            numberOfTracks = playlist.numberOfTracks
        )
    }

    private fun map(playlistEntity: PlaylistEntity): Playlist {
        return Playlist(
            id = playlistEntity.id,
            title = playlistEntity.title,
            description = playlistEntity.description,
            coverUri = playlistEntity.coverUri?.toUri(),
            tracks = playlistEntity.tracks.split(","),
            numberOfTracks = playlistEntity.numberOfTracks
        )
    }

    companion object {
        private const val ALBUM_NAME = "PlaylistsAlbum"
    }
}