package com.example.playlistmaker.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity: AppCompatActivity() {

    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        viewModel.darkThemeState.observe(this) {
            binding.themeSwitcher.isChecked = it
        }
    }

    private fun setClickListeners() {
        binding.backButton.setOnClickListener { finish() }
        binding.shareAppButton.setOnClickListener { viewModel.shareApp() }
        binding.writeToSupportButton.setOnClickListener { viewModel.writeToSupport() }
        binding.userAgreementButton.setOnClickListener { viewModel.openUserAgreement() }
        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.switchDarkTheme(checked)
        }
    }
}