package com.example.playlistmaker.presentation.mappers

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.ui.models.TrackInfo
import java.time.LocalDateTime

object TrackMapper {

    fun map(track: Track): TrackInfo {
        return TrackInfo(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            artworkUrl100 = track.artworkUrl100,
            coverArtwork = track.artworkUrl100?.replaceAfterLast('/',"512x512bb.jpg"),
            country = track.country,
            trackTime = track.trackTime,
            genreName = track.genreName,
            albumName = track.albumName,
            releaseYear = getReleaseYearFrom(track)
        )
    }

    private fun getReleaseYearFrom(track: Track): String? {
        if (track.releaseDate == null) return null
        return LocalDateTime
            .parse(track.releaseDate.removeSuffix("Z")).year
            .toString()
    }
}