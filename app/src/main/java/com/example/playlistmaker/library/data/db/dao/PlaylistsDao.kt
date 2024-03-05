package com.example.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.library.data.db.entities.PlaylistEntity

@Dao
interface PlaylistsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlists_table")
    suspend fun getPlaylists(): List<PlaylistEntity>
}