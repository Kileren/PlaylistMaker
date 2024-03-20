package com.example.playlistmaker.library.ui.newPlaylist

import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.Playlist
import com.example.playlistmaker.library.domain.newPlaylist.NewPlaylistInteractor
import com.example.playlistmaker.library.domain.playlists.PlaylistsInteractor
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val interactor: NewPlaylistInteractor,
    private val playlistsInteractor: PlaylistsInteractor
): ViewModel() {

    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    private val coverLiveData = MutableLiveData<Uri?>()
    fun observeCover(): LiveData<Uri?> = coverLiveData

    private val stateLiveData = MutableLiveData<NewPlaylistState>()
    fun observeState(): LiveData<NewPlaylistState> = stateLiveData

    private var editablePlaylistId: Int? = null

    fun onCreateView(fragment: NewPlaylistFragment) {
        createPickMedia(fragment)

        val arguments = fragment.arguments ?: return
        val playlistId = arguments.getString(NewPlaylistFragment.editPlaylistId) ?: return

        editablePlaylistId = playlistId.toInt()
        val imageUri = arguments.getString(NewPlaylistFragment.editImageKey)
        val title = arguments.getString(NewPlaylistFragment.editTitleKey)
        val description = arguments.getString(NewPlaylistFragment.editDescriptionKey)

        if (imageUri != null) {
            coverLiveData.postValue(imageUri.toUri())
        }
        if (title != null || description != null) {
            val state = NewPlaylistState.Edit(imageUri == null, title, description)
            stateLiveData.postValue(state)
        }
    }

    fun selectMedia() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    fun createPlaylist(title: String, description: String?) {
        val editablePlaylistId = this.editablePlaylistId
        if (editablePlaylistId != null) {
            updateExistingPlaylist(editablePlaylistId, title, description)
        } else {
            createNewPlaylist(title, description)
        }
    }

    private fun updateExistingPlaylist(id: Int, title: String, description: String?) {
        viewModelScope.launch {
            playlistsInteractor.getPlaylist(id).collect() { playlist ->
                if (playlist == null) {
                    assert(false) { "Playlist must exists, check that id is correct" }
                    return@collect
                }

                val uri = coverLiveData.value
                if (uri != null && uri != interactor.getCoverImage(playlist.title)) {
                    interactor.saveCoverImage(uri, title)
                } else if (playlist.title != title) {
                    val imageUri = interactor.getCoverImage(playlist.title)
                    if (imageUri != null) {
                        interactor.saveCoverImage(imageUri, title)
                    }
                }

                val updatedPlaylist = playlist.copy(
                    title = title,
                    description = description,
                    coverUri = interactor.getCoverImage(title)
                )
                playlistsInteractor.updatePlaylist(updatedPlaylist)
                stateLiveData.postValue(NewPlaylistState.Close)
            }
        }
    }

    private fun createNewPlaylist(title: String, description: String?) {
        val uri = coverLiveData.value
        if (uri != null) {
            interactor.saveCoverImage(uri, title)
        }

        val playlist = Playlist(
            id = 0,
            title = title,
            description = description,
            coverUri = interactor.getCoverImage(title),
            tracks = listOf()
        )

        viewModelScope.launch {
            interactor.createPlaylist(playlist)
            stateLiveData.postValue(NewPlaylistState.Close)
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