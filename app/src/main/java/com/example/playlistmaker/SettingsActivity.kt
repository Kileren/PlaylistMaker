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
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.practicum_android_course_link))
        startActivity(shareIntent)
    }

    private fun writeToSupport() {
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_message_email)))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_message_theme))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.support_message_body))
            startActivity(this)
        }
    }

    private fun openUserAgreement() {
        val agreementURL = getString(R.string.practicum_user_agreement_link)
        val browserIntent = Intent(Intent.ACTION_VIEW)
        browserIntent.data = Uri.parse(agreementURL)
        startActivity(browserIntent)
    }
}