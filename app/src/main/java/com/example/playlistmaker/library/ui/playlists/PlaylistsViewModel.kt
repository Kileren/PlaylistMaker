package com.example.playlistmaker.library.ui.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.playlists.PlaylistsInteractor
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val interactor: PlaylistsInteractor
): ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistsState>()
    fun observeState(): LiveData<PlaylistsState> = stateLiveData

    init {
        updateData()
    }

    fun updateData() {
        viewModelScope.launch {
            interactor.getPlaylists().collect() {playlists ->
                if (playlists.isEmpty()) {
                    stateLiveData.postValue(PlaylistsState.Empty)
                } else {
                    stateLiveData.postValue(PlaylistsState.Content(playlists))
                }
            }
        }
    }
}