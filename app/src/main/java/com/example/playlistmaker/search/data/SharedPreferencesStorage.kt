package com.example.playlistmaker.search.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit

class SharedPreferencesStorage(private val context: Context) {

    private companion object {
        const val storageKey = "com.practicum.playlistMaker.sharedPreferencesStorage"
        const val darkThemeKey = "darkThemeKey"
    }

    private val prefs by lazy { context.getSharedPreferences(storageKey, MODE_PRIVATE) }

    /**
     * Flag indicates whether dark theme enabled or not
     */
    var darkTheme: Boolean
        get() {
            return prefs.getBoolean(darkThemeKey, false)
        }
        set(value) {
            prefs.edit {
                putBoolean(darkThemeKey, value)
            }
        }
}