package com.example.playlistmaker.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity: AppCompatActivity() {

    private val viewModel by viewModel<SettingsViewModel>()
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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