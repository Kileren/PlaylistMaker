package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.storage.SharedPreferencesStorage

class App: Application() {

    private val sharedPreferencesStorage by lazy {
        SharedPreferencesStorage(this)
    }

    override fun onCreate() {
        super.onCreate()
        switchTheme(darkThemeEnabled())
    }

    fun darkThemeEnabled() = sharedPreferencesStorage.darkTheme

    fun switchTheme(darkThemeEnabled: Boolean) {
        sharedPreferencesStorage.darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}