package com.example.playlistmaker.presentation.api

import com.example.playlistmaker.presentation.ui.models.TrackInfo

interface AudioPlayer {

    fun configure(model: TrackInfo)
    fun updatePlaybackTime(value: String)

    fun showPlayButton()
    fun showPauseButton()

    companion object {
        const val trackKey = "audioPlayer.track"
    }
}