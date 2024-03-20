package com.example.playlistmaker.sharing.domain

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator
): SharingInteractor {

    override fun shareApp() {
        externalNavigator.shareApp()
    }

    override fun shareText(text: String) {
        externalNavigator.shareText(text)
    }

    override fun openTerms() {
        externalNavigator.openTerms()
    }

    override fun writeToSupport() {
        externalNavigator.writeEmail()
    }
}