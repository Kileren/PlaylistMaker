package com.example.playlistmaker.sharing.domain

interface ExternalNavigator {
    fun shareApp()
    fun shareText(text: String)
    fun openTerms()
    fun writeEmail()
}