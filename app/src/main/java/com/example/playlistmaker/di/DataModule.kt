package com.example.playlistmaker.di

import android.media.MediaPlayer
import com.example.playlistmaker.player.data.PlayerImpl
import com.example.playlistmaker.player.domain.api.Player
import com.example.playlistmaker.search.data.SearchHistoryStorageImpl
import com.example.playlistmaker.search.data.SharedPreferencesStorage
import com.example.playlistmaker.search.data.network.Endpoint
import com.example.playlistmaker.search.data.network.ITunesApi
import com.example.playlistmaker.search.data.network.ITunesService
import com.example.playlistmaker.search.domain.SearchHistoryStorage
import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.data.ExternalNavigatorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<SharedPreferencesStorage> {
        SharedPreferencesStorage(androidContext())
    }

    single<SearchHistoryStorage> {
        SearchHistoryStorageImpl(androidContext())
    }

    factory {
        ITunesService(get())
    }

    single {
        Retrofit.Builder()
            .baseUrl(Endpoint.ITUNES.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java)
    }

    factory<Player> {
        PlayerImpl(get())
    }

    factory {
        MediaPlayer()
    }

    factory<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }
}