package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity: AppCompatActivity() {

    private lateinit var backButton: ImageView
    private lateinit var shareAppButton: View
    private lateinit var writeToSupportButton: View
    private lateinit var userAgreementButton: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setButtons()
        setClickListeners()
    }

    private fun setButtons() {
        backButton = findViewById(R.id.back_button)
        shareAppButton = findViewById(R.id.share_app_button)
        writeToSupportButton = findViewById(R.id.write_to_support_button)
        userAgreementButton = findViewById(R.id.user_agreement_button)
    }

    private fun setClickListeners() {
        backButton.setOnClickListener { finish() }
        shareAppButton.setOnClickListener { shareApp() }
        writeToSupportButton.setOnClickListener { writeToSupport() }
        userAgreementButton.setOnClickListener { openUserAgreement() }
    }

    private fun shareApp() {
        val urlToShare = "https://practicum.yandex.ru/profile/android-developer/"
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, urlToShare)
        startActivity(shareIntent)
    }

    private fun writeToSupport() {
        val mailIntent = Intent(Intent.ACTION_SENDTO)
        mailIntent.data = Uri.parse("mailto:")
        mailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("gileren8613@yandex.ru"))
        mailIntent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.support_message_theme))
        mailIntent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.support_message_body))
        startActivity(mailIntent)
    }

    private fun openUserAgreement() {
        val agreementURL = "https://yandex.ru/legal/practicum_offer/"
        val browserIntent = Intent(Intent.ACTION_VIEW)
        browserIntent.data = Uri.parse(agreementURL)
        startActivity(browserIntent)
    }
}