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

    fun onCreate(fragment: PlaylistFragment, context: Context) {
        val playlistID = fragment.requireArguments().getInt(PlaylistFragment.playlistKey)

        viewModelScope.launch {
            interactor.getPlaylist(playlistID).collect() { playlist ->
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
            val tracks = context.resources.getQuantityString(R.plurals.number_of_tracks, 0, 0)
            return "$duration • $tracks"
        }

        val formatterToSeconds = SimpleDateFormat("mm:ss")
        val totalDurationSeconds = tracks
            .map { it.trackTime }
            .map { trackTime ->
                val parsedValue = formatterToSeconds.parse(trackTime)
                (parsedValue?.minutes ?: 0) * 60 + (parsedValue?.seconds ?: 0)
            }
            .reduce { acc, time -> acc + time }
        val totalDuration = SimpleDateFormat("mm", Locale.getDefault()).format(totalDurationSeconds * 1000)
        val totalDurationMinutes = SimpleDateFormat("mm").parse(totalDuration)?.minutes ?: 0
        val duration = context.resources.getQuantityString(
            R.plurals.minutes,
            totalDurationMinutes,
            totalDurationMinutes
        )

        val tracks = context.resources.getQuantityString(
            R.plurals.number_of_tracks,
            tracks.size,
            tracks.size
        )

        return "$duration • $tracks"
    }
}