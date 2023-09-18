package com.example.playlistmaker.storage

import android.content.Context
import android.content.Context.MODE_PRIVATE

class SharedPreferencesStorage(private val context: Context) {

    private companion object {
        const val storageKey = "com.practicum.playlistMaker.sharedPreferencesStorage"

        const val darkThemeKey = "darkThemeKey"
    }

    private val prefs = context.getSharedPreferences(storageKey, MODE_PRIVATE)

    var darkTheme: Boolean
        get() {
            return prefs.getBoolean(darkThemeKey, false)
        }
        set(value) {
            prefs.edit()
                .putBoolean(darkThemeKey, value)
                .apply()
        }
}