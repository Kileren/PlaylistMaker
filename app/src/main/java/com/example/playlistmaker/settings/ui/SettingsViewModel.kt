package com.example.playlistmaker.settings.ui

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(
    private val interactor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor
): ViewModel() {

    private val _darkThemeState = MutableLiveData<Boolean>()
    val darkThemeState: LiveData<Boolean> = _darkThemeState

    init {
        _darkThemeState.postValue(interactor.getDarkThemeState())
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun writeToSupport() {
        sharingInteractor.writeToSupport()
    }

    fun openUserAgreement() {
        sharingInteractor.openTerms()
    }

    fun switchDarkTheme(isDark: Boolean) {
        interactor.setDarkThemeState(isDark)
        _darkThemeState.postValue(isDark)
        AppCompatDelegate.setDefaultNightMode(
            if (isDark) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}