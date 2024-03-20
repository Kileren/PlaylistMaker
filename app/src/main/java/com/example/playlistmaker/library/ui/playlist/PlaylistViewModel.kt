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
import com.example.playlistmaker.sharing.domain.SharingInteractor
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistViewModel(
    private val interactor: PlaylistsInteractor,
    private val sharingInteractor: SharingInteractor
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

    fun sharePlaylist(context: Context) {
        val value = state.value as? PlaylistState.Content ?: return
        var shareInfo = ""

        shareInfo += value.title + "\n"
        if (value.description != null) {
            shareInfo += value.description + "\n"
        }
        shareInfo += context.resources.getQuantityString(
            R.plurals.number_of_tracks,
            value.tracks.size,
            value.tracks.size
        ) + "\n"
        value.tracks.forEachIndexed { index, track ->
            shareInfo += "${index + 1}. ${track.artistName} - ${track.trackName} (${track.trackTime})\n"
        }

        sharingInteractor.shareText(shareInfo)
    }

    fun deletePlaylist() {
        val id = playlistId ?: return

        viewModelScope.launch {
            interactor.deletePlaylist(id)
            state.postValue(PlaylistState.Removed)
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
            totalDuration = getTotalDuration(tracks, context),
            totalTracks = getTotalTracks(tracks, context),
            tracks = tracks
        )
        this.state.postValue(state)
    }

    private fun getTotalDuration(tracks: List<Track>, context: Context): String {
        if (tracks.isEmpty()) {
            return context.resources.getQuantityString(R.plurals.minutes, 0, 0)
        }

        val totalDurationMillis = tracks
            .map { it.trackTimeMillis }
            .reduce { acc, time -> acc + time }
        val totalDuration = SimpleDateFormat("mm", Locale.getDefault()).format(totalDurationMillis).toInt()
        return context.resources.getQuantityString(
            R.plurals.minutes,
            totalDuration,
            totalDuration
        )
    }

    private fun getTotalTracks(tracks: List<Track>, context: Context): String {
        if (tracks.isEmpty()) {
            return context.resources.getQuantityString(R.plurals.number_of_tracks, 0, 0)
        }
        return context.resources.getQuantityString(
            R.plurals.number_of_tracks,
            tracks.size,
            tracks.size
        )
    }
}