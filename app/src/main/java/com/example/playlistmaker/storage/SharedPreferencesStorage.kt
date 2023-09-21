package com.example.playlistmaker.storage

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.playlistmaker.models.Track
import com.google.gson.Gson

class SharedPreferencesStorage(private val context: Context) {

    private companion object {
        const val storageKey = "com.practicum.playlistMaker.sharedPreferencesStorage"

        const val darkThemeKey = "darkThemeKey"
        const val searchHistoryKey = "searchHistoryKey"
    }

    private val prefs = context.getSharedPreferences(storageKey, MODE_PRIVATE)

    /**
     * Flag indicates whether dark theme enabled or not
     */
    var darkTheme: Boolean
        get() {
            return prefs.getBoolean(darkThemeKey, false)
        }
        set(value) {
            prefs.edit()
                .putBoolean(darkThemeKey, value)
                .apply()
        }

    /**
     * Tracks that user searched lately
     */
    var searchHistory: Array<Track>
        get() {
            val json = prefs.getString(searchHistoryKey, null) ?: return emptyArray()
            return Gson().fromJson(json, Array<Track>::class.java)
        }
        set(value) {
            val json = Gson().toJson(value)
            prefs.edit()
                .putString(searchHistoryKey, json)
                .apply()
        }
}