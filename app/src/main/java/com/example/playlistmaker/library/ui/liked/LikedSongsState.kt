package com.example.playlistmaker.library.ui.liked

import com.example.playlistmaker.search.domain.Track

sealed interface LikedSongsState {

    object Empty: LikedSongsState

    data class Content(
        val tracks: List<Track>
    ): LikedSongsState
}