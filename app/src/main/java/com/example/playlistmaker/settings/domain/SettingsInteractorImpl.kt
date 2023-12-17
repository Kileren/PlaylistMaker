package com.example.playlistmaker.settings.domain

class SettingsInteractorImpl(
    private val repository: SettingsRepository
): SettingsInteractor {
    override fun getDarkThemeState(): Boolean {
        return repository.getDarkThemeState()
    }
    override fun setDarkThemeState(isDark: Boolean) {
        return repository.setDarkThemeState(isDark)
    }
}