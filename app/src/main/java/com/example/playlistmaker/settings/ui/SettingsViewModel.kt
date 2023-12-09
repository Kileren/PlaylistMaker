package com.example.playlistmaker.settings.ui

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(
    private val interactor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor
): ViewModel() {

    private val darkThemeState = MutableLiveData<Boolean>()
    fun getDarkThemeState(): LiveData<Boolean> = darkThemeState

    init {
        darkThemeState.postValue(interactor.getDarkThemeState())
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
        darkThemeState.postValue(isDark)
        AppCompatDelegate.setDefaultNightMode(
            if (isDark) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        fun getViewModelFactory(
            settingsInteractor: SettingsInteractor,
            sharingInteractor: SharingInteractor
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(settingsInteractor, sharingInteractor)
            }
        }
    }
}