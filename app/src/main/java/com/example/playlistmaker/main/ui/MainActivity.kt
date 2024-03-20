package com.example.playlistmaker.main.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.newPlaylistFragment,
                R.id.audioPlayerFragment,
                R.id.playlistFragment -> {
                    binding.bottomNavigationBar.isVisible = false
                    binding.bottomNavigationBarSeparator.isVisible = false
                }
                else -> {
                    binding.bottomNavigationBar.isVisible = true
                    binding.bottomNavigationBarSeparator.isVisible = true
                }
            }
        }

        binding.bottomNavigationBar.setupWithNavController(navController)
    }
}