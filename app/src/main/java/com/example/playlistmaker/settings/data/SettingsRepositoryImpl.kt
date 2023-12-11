package com.example.playlistmaker.settings.data

import com.example.playlistmaker.search.data.SharedPreferencesStorage
import com.example.playlistmaker.settings.domain.SettingsRepository

class SettingsRepositoryImpl(
    private val prefs: SharedPreferencesStorage
): SettingsRepository {

    override fun getDarkThemeState(): Boolean = prefs.darkTheme
    override fun setDarkThemeState(isDark: Boolean) {
        prefs.darkTheme = isDark
    }
}