package com.example.playlistmaker.library.ui.newPlaylist

import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.Playlist
import com.example.playlistmaker.library.domain.newPlaylist.NewPlaylistInteractor
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val interactor: NewPlaylistInteractor
): ViewModel() {

    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    private val coverLiveData = MutableLiveData<Uri?>()
    fun observeCover(): LiveData<Uri?> = coverLiveData

    fun onCreateView(fragment: NewPlaylistFragment) {
        createPickMedia(fragment)
    }

    fun selectMedia() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    fun createPlaylist(title: String, description: String?) {
        val uri = coverLiveData.value
        if (uri != null) {
            interactor.saveCoverImage(uri, title)
        }
        val playlist = Playlist(
            id = 0,
            title = title,
            description = description,
            coverUri = interactor.getCoverImage(title),
            numberOfTracks = 0
        )

        viewModelScope.launch {
            interactor.createPlaylist(playlist)
        }
    }

    private fun createPickMedia(fragment: NewPlaylistFragment) {
        pickMedia = fragment.registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                coverLiveData.postValue(uri)
            } else {
                Log.d("NewPlaylistViewModel", "No media selected")
            }
        }
    }
}