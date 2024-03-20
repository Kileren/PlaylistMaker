package com.example.playlistmaker.library.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_tracks_table")
data class TrackEntity(
    @PrimaryKey
    val id: String,
    val trackName: String,
    val artistName: String,
    val artworkUrl: String?,
    val country: String,
    val trackTimeMillis: Long,
    val releaseDate: String?,
    val previewUrl: String?,
    val genreName: String,
    val albumName: String?
)
