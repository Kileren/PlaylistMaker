package com.example.playlistmaker

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.interactorModule
import com.example.playlistmaker.di.repositoryModule
import com.example.playlistmaker.di.viewModelModule
import com.example.playlistmaker.search.data.SharedPreferencesStorage
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {

    private val sharedPreferencesStorage by lazy {
        val storage: SharedPreferencesStorage = getKoin().get()
        storage
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, interactorModule, repositoryModule, viewModelModule)
        }

        if (sharedPreferencesStorage.isFirstLaunch) {
            sharedPreferencesStorage.isFirstLaunch = false
            setSystemTheme()
        } else {
            switchTheme()
        }
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

    private fun setSystemTheme() {
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> sharedPreferencesStorage.darkTheme = true
            Configuration.UI_MODE_NIGHT_NO -> sharedPreferencesStorage.darkTheme = false
            else -> return
        }
        switchTheme()
    }
}