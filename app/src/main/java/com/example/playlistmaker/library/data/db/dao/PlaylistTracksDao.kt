package com.example.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.playlistmaker.library.data.db.entities.PlaylistTrackEntity

@Dao
interface PlaylistTracksDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrack(trackEntity: PlaylistTrackEntity)
}