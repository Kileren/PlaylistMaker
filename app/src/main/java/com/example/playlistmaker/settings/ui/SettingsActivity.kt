package com.example.playlistmaker.settings.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity: ComponentActivity() {

    private lateinit var viewModel: SettingsViewModel

    private val backButton: ImageView by lazy { findViewById(R.id.back_button) }
    private val themeSwitcher: SwitchMaterial by lazy { findViewById(R.id.theme_switcher) }
    private val shareAppButton: View by lazy { findViewById(R.id.share_app_button) }
    private val writeToSupportButton: View by lazy { findViewById(R.id.write_to_support_button) }
    private val userAgreementButton: View by lazy { findViewById(R.id.user_agreement_button) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory(
                Creator.createSettingsInteractor(this),
                Creator.createSharingInteractor(this)
            )
        ).get()
        setObservers()
        setClickListeners()
    }

    private fun setObservers() {
        viewModel.getDarkThemeState().observe(this) {
            themeSwitcher.isChecked = it
        }
    }

    private fun setClickListeners() {
        backButton.setOnClickListener { finish() }
        shareAppButton.setOnClickListener { viewModel.shareApp() }
        writeToSupportButton.setOnClickListener { viewModel.writeToSupport() }
        userAgreementButton.setOnClickListener { viewModel.openUserAgreement() }
        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.switchDarkTheme(checked)
        }
    }
}