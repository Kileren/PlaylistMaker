package com.example.playlistmaker.library.ui.playlist

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.library.domain.Playlist
import com.example.playlistmaker.library.domain.playlists.PlaylistsInteractor
import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistViewModel(
    private val interactor: PlaylistsInteractor,
): ViewModel() {

    private val state = MutableLiveData<PlaylistState>()
    fun observeState(): LiveData<PlaylistState> = state

    private var playlistId: Int? = null

    fun onCreate(fragment: PlaylistFragment, context: Context) {
        val playlistId = fragment.requireArguments().getInt(PlaylistFragment.playlistKey)
        this.playlistId = playlistId

        viewModelScope.launch {
            reload(playlistId, context)
        }
    }

    fun removeTrack(track: Track, context: Context) {
        val playlistId = this.playlistId ?: return

        viewModelScope.launch {
            interactor.removeTrackFromPlaylist(playlistId, track.trackId)
            reload(playlistId, context)
        }
    }

    private suspend fun reload(playlistId: Int, context: Context) {
        interactor.getPlaylist(playlistId).collect() { playlist ->
            if (playlist == null) {
                assert(false) { "Playlist must exists, check that id is correct" }
                return@collect
            }

            interactor.getTracksInPlaylist(playlist)
                .collect {
                    updateState(playlist, it, context)
                }
        }
    }

    private fun updateState(playlist: Playlist, tracks: List<Track>, context: Context) {
        val state = PlaylistState.Content(
            coverUri = playlist.coverUri,
            title = playlist.title,
            description = if (playlist.description.isNullOrEmpty()) null else playlist.description,
            additionalInfoText = getAdditionalInfoText(tracks, context),
            tracks = tracks
        )
        this.state.postValue(state)
    }

    private fun getAdditionalInfoText(tracks: List<Track>, context: Context): String {
        if (tracks.isEmpty()) {
            val duration = context.resources.getQuantityString(R.plurals.minutes, 0, 0)
            val tracksCount = context.resources.getQuantityString(R.plurals.number_of_tracks, 0, 0)
            return "$duration • $tracksCount"
        }

        val totalDurationMillis = tracks
            .map { it.trackTimeMillis }
            .reduce { acc, time -> acc + time }
        val totalDuration = SimpleDateFormat("mm", Locale.getDefault()).format(totalDurationMillis).toInt()
        val duration = context.resources.getQuantityString(
            R.plurals.minutes,
            totalDuration,
            totalDuration
        )
        val tracksCount = context.resources.getQuantityString(
            R.plurals.number_of_tracks,
            tracks.size,
            tracks.size
        )

        return "$duration • $tracksCount"
    }
}