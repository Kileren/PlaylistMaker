package com.example.playlistmaker

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity: AppCompatActivity() {

    private lateinit var backButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setButtons()
        setClickListeners()
    }

    private fun setButtons() {
        backButton = findViewById(R.id.back_button)
    }

    private fun setClickListeners() {
        backButton.setOnClickListener {
            finish()
        }
    }
}