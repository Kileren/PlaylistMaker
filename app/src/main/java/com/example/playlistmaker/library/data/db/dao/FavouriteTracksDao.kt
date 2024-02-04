package com.example.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.library.data.db.entities.TrackEntity

@Dao
interface FavouriteTracksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrack(track: TrackEntity)

    @Delete
    suspend fun removeTrack(track: TrackEntity)

    @Query("SELECT * FROM favourite_tracks_table")
    suspend fun getTracks(): List<TrackEntity>

    @Query("SELECT id FROM favourite_tracks_table")
    suspend fun getTracksIDs(): List<String>
}