package com.example.playlistmaker.library.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_tracks_table")
data class PlaylistTrackEntity(
    @PrimaryKey
    val id: String,
    val trackName: String,
    val artistName: String,
    val artworkUrl: String?,
    val country: String,
    val trackTime: String,
    val releaseDate: String?,
    val previewUrl: String?,
    val genreName: String,
    val albumName: String?
)