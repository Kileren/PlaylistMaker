package com.example.playlistmaker.settings.domain

class SettingsInteractorImpl(
    private val repository: SettingsRepository
): SettingsInteractor {
    override fun getDarkThemeState(): Boolean = repository.getDarkThemeState()
    override fun setDarkThemeState(isDark: Boolean) = repository.setDarkThemeState(isDark)
}