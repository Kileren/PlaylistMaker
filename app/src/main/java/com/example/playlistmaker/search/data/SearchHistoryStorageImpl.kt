package com.example.playlistmaker.search.data

import android.content.Context
import androidx.core.content.edit
import com.example.playlistmaker.search.domain.SearchHistoryStorage
import com.example.playlistmaker.search.domain.Track
import com.google.gson.Gson

class SearchHistoryStorageImpl(private val context: Context): SearchHistoryStorage {

    private companion object {
        const val storageKey = "com.practicum.playlistMaker.searchHistoryStorage"
        const val searchHistoryKey = "searchHistoryKey"
    }

    private val prefs by lazy { context.getSharedPreferences(storageKey, Context.MODE_PRIVATE) }

    /**
     * Tracks that user searched lately
     */
    override var searchHistory: Array<Track>
        get() {
            val json = prefs.getString(searchHistoryKey, null) ?: return emptyArray()
            return Gson().fromJson(json, Array<Track>::class.java)
        }
        set(value) {
            val json = Gson().toJson(value)
            prefs.edit {
                putString(searchHistoryKey, json)
            }
        }

    /**
     * Returns track from search history by id
     */
    override fun getTrack(id: String): Track {
        return searchHistory.first { it.trackId == id }
    }

    /**
     * Adds track to history
     */
    override fun addTrackToHistory(track: Track) {
        var history = searchHistory.toMutableList()
        history.removeIf { it.trackId == track.trackId }
        history.add(0, track)
        if (history.size > 10) {
            history = history.subList(0, 10)
        }
        searchHistory = history.toTypedArray()
    }
}