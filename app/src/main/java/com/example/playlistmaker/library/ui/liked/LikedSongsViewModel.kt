package com.example.playlistmaker.library.ui.liked

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.FavouriteTracksInteractor
import kotlinx.coroutines.launch

class LikedSongsViewModel(
    private val interactor: FavouriteTracksInteractor
): ViewModel() {

    private val stateLiveData = MutableLiveData<LikedSongsState>()

    fun observeState(): LiveData<LikedSongsState> = stateLiveData

    init {
        updateData()
    }

    fun updateData() {
        viewModelScope.launch {
            interactor.getTracks().collect() { tracks ->
                if (tracks.isEmpty()) {
                    stateLiveData.postValue(LikedSongsState.Empty)
                } else {
                    stateLiveData.postValue(LikedSongsState.Content(tracks))
                }
            }
        }
    }
}