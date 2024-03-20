package com.example.playlistmaker.library.data.db

import com.example.playlistmaker.library.data.db.entities.PlaylistTrackEntity
import com.example.playlistmaker.library.data.db.entities.TrackEntity
import com.example.playlistmaker.search.domain.Track

class TrackDbConverter {

    fun map(track: Track): TrackEntity {
        return TrackEntity(
            id = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            artworkUrl = track.artworkUrl100,
            country = track.country,
            trackTimeMillis = track.trackTimeMillis,
            releaseDate = track.releaseDate,
            previewUrl = track.previewUrl,
            genreName = track.genreName,
            albumName = track.albumName
        )
    }

    fun map(trackEntity: TrackEntity): Track {
        return Track(
            trackId = trackEntity.id,
            trackName = trackEntity.trackName,
            artistName = trackEntity.artistName,
            artworkUrl100 = trackEntity.artworkUrl,
            country = trackEntity.country,
            trackTimeMillis = trackEntity.trackTimeMillis,
            genreName = trackEntity.genreName,
            releaseDate = trackEntity.releaseDate,
            previewUrl = trackEntity.previewUrl,
            albumName = trackEntity.albumName
        )
    }

    fun mapForPlaylist(track: Track): PlaylistTrackEntity {
        return PlaylistTrackEntity(
            id = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            artworkUrl = track.artworkUrl100,
            country = track.country,
            trackTimeMillis = track.trackTimeMillis,
            releaseDate = track.releaseDate,
            previewUrl = track.previewUrl,
            genreName = track.genreName,
            albumName = track.albumName
        )
    }

    fun mapFromPlaylist(trackEntity: PlaylistTrackEntity): Track {
        return Track(
            trackId = trackEntity.id,
            trackName = trackEntity.trackName,
            artistName = trackEntity.artistName,
            artworkUrl100 = trackEntity.artworkUrl,
            country = trackEntity.country,
            trackTimeMillis = trackEntity.trackTimeMillis,
            genreName = trackEntity.genreName,
            releaseDate = trackEntity.releaseDate,
            previewUrl = trackEntity.previewUrl,
            albumName = trackEntity.albumName
        )
    }
}