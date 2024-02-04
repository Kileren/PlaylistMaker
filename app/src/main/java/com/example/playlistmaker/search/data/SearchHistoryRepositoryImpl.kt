package com.example.playlistmaker.search.data

import android.content.Context
import androidx.core.content.edit
import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.search.domain.SearchHistoryRepository
import com.example.playlistmaker.search.domain.Track
import com.google.gson.Gson

class SearchHistoryRepositoryImpl(
    private val context: Context,
    private val appDatabase: AppDatabase
): SearchHistoryRepository {

    private companion object {
        const val storageKey = "com.practicum.playlistMaker.searchHistoryRepository"
        const val searchHistoryKey = "searchHistoryKey"
    }

    private val prefs by lazy { context.getSharedPreferences(storageKey, Context.MODE_PRIVATE) }

    /**
     * Tracks that user searched lately
     */
    override suspend fun getSearchHistory(): List<Track> {
        val json = prefs.getString(searchHistoryKey, null) ?: return emptyList()
        val tracks = Gson().fromJson(json, Array<Track>::class.java).toList()
        val favouriteTracksIDs = appDatabase.favouriteTracksDao().getTracksIDs()
        tracks.forEach {
            it.isFavourite = favouriteTracksIDs.contains(it.trackId)
        }
        return tracks
    }

    /**
     * Replace history with new value
     */
    override suspend fun setSearchHistory(history: List<Track>) {
        val json = Gson().toJson(history)
        prefs.edit {
            putString(searchHistoryKey, json)
        }
    }

    /**
     * Returns track from search history by id
     */
    override suspend fun getTrack(id: String): Track {
        return getSearchHistory().first { it.trackId == id }
    }

    /**
     * Adds track to history
     */
    override suspend fun addTrackToHistory(track: Track) {
        var history = getSearchHistory().toMutableList()
        history.removeIf { it.trackId == track.trackId }
        history.add(0, track)
        if (history.size > 10) {
            history = history.subList(0, 10)
        }
        setSearchHistory(history)
    }
}