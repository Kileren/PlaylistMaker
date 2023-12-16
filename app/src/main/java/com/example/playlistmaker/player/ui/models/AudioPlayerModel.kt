package com.example.playlistmaker.player.ui

import com.example.playlistmaker.player.ui.models.AudioPlayerPlayButtonState

data class AudioPlaybackModel(
    val playbackTime: String = "",
    val playButtonState: AudioPlayerPlayButtonState? = null
)