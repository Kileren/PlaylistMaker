package com.example.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.library.data.db.entities.PlaylistTrackEntity

@Dao
interface PlaylistTracksDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrack(trackEntity: PlaylistTrackEntity)

    @Query("SELECT * FROM playlist_tracks_table WHERE id IN (:ids)")
    suspend fun getTracks(ids: List<String>): List<PlaylistTrackEntity>

    @Query("DELETE FROM playlist_tracks_table WHERE id == :trackId")
    suspend fun removeTrack(trackId: String)
}