package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator

class App: Application() {

    private val sharedPreferencesStorage by lazy {
        Creator.createSharedPreferencesStorage(this)
    }

    override fun onCreate() {
        super.onCreate()
        switchTheme()
    }

    private fun switchTheme() {
        val darkThemeEnabled = sharedPreferencesStorage.darkTheme
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}