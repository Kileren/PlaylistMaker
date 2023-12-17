package com.example.playlistmaker.settings.domain

interface SettingsRepository {
    fun getDarkThemeState(): Boolean
    fun setDarkThemeState(isDark: Boolean)
}