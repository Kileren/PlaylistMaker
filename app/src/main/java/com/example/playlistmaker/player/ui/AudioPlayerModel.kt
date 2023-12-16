package com.example.playlistmaker.player.ui

data class AudioPlaybackModel(
    val playbackTime: String = "",
    val playButtonState: AudioPlayerPlayButtonState = AudioPlayerPlayButtonState.PLAY
)

enum class AudioPlayerPlayButtonState {
    PLAY,
    PAUSE
}