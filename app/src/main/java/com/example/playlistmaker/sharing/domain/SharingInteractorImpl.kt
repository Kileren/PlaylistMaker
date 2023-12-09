package com.example.playlistmaker.sharing.domain

import com.example.playlistmaker.sharing.data.ExternalNavigator

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator
): SharingInteractor {

    override fun shareApp() {
        externalNavigator.shareApp()
    }

    override fun openTerms() {
        externalNavigator.openTerms()
    }

    override fun writeToSupport() {
        externalNavigator.writeEmail()
    }
}