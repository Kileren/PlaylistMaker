package com.example.playlistmaker.library.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.library.data.db.dao.FavouriteTracksDao
import com.example.playlistmaker.library.data.db.dao.PlaylistTracksDao
import com.example.playlistmaker.library.data.db.dao.PlaylistsDao
import com.example.playlistmaker.library.data.db.entities.PlaylistEntity
import com.example.playlistmaker.library.data.db.entities.PlaylistTrackEntity
import com.example.playlistmaker.library.data.db.entities.TrackEntity

@Database(
    version = 1,
    entities = [
        TrackEntity::class,
        PlaylistEntity::class,
        PlaylistTrackEntity::class
    ]
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun favouriteTracksDao(): FavouriteTracksDao
    abstract fun playlistsDao(): PlaylistsDao

    abstract fun playlistTracksDao(): PlaylistTracksDao
}