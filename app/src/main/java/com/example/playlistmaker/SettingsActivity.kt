package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity: AppCompatActivity() {

    private val backButton: ImageView by lazy { findViewById(R.id.back_button) }
    private val themeSwitcher: SwitchMaterial by lazy { findViewById(R.id.theme_switcher) }
    private val shareAppButton: View by lazy { findViewById(R.id.share_app_button) }
    private val writeToSupportButton: View by lazy { findViewById(R.id.write_to_support_button) }
    private val userAgreementButton: View by lazy { findViewById(R.id.user_agreement_button) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        configureUI()
        setClickListeners()
    }

    private fun configureUI() {
        themeSwitcher.isChecked = (applicationContext as App).darkThemeEnabled()
    }

    private fun setClickListeners() {
        backButton.setOnClickListener { finish() }
        shareAppButton.setOnClickListener { shareApp() }
        writeToSupportButton.setOnClickListener { writeToSupport() }
        userAgreementButton.setOnClickListener { openUserAgreement() }
        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
        }
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