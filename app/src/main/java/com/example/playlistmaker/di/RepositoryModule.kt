package com.example.playlistmaker.di

import com.example.playlistmaker.search.data.SearchRepositoryImpl
import com.example.playlistmaker.search.domain.SearchRepository
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.SettingsRepository
import org.koin.dsl.module

val repositoryModule = module {

    factory<SearchRepository> {
        SearchRepositoryImpl(get())
    }

    factory<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }
}