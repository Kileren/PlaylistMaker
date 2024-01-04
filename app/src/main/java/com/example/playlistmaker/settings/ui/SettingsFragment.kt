package com.example.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment: Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setUpAndConfigureBindings(inflater, container)
        setObservers()
        setClickListeners()
        return binding.root
    }

    private fun setUpAndConfigureBindings(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
    }

    private fun setObservers() {
        viewModel.darkThemeState.observe(viewLifecycleOwner) {
            binding.themeSwitcher.isChecked = it
        }
    }

    private fun setClickListeners() {
        binding.shareAppButton.setOnClickListener { viewModel.shareApp() }
        binding.writeToSupportButton.setOnClickListener { viewModel.writeToSupport() }
        binding.userAgreementButton.setOnClickListener { viewModel.openUserAgreement() }
        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.switchDarkTheme(checked)
        }
    }
}