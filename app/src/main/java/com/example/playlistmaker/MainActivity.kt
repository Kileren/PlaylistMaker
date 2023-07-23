package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var searchButton: Button
    private lateinit var mediaLibraryButton: Button
    private lateinit var settingsButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setButtons()
        setClickListeners()
    }

    private fun setButtons() {
        searchButton = findViewById<Button>(R.id.search_button)
        mediaLibraryButton = findViewById<Button>(R.id.media_library_button)
        settingsButton = findViewById<Button>(R.id.settings_button)
    }
    private fun setClickListeners() {
        val searchButtonClickListener = object : View.OnClickListener {
            override fun onClick(p0: View?) {
                Toast.makeText(this@MainActivity, "Search button tapped", Toast.LENGTH_SHORT).show()
            }
        }
        searchButton.setOnClickListener(searchButtonClickListener)

        mediaLibraryButton.setOnClickListener {
            Toast.makeText(this@MainActivity, "Media Library button tapped", Toast.LENGTH_SHORT).show()
        }

        settingsButton.setOnClickListener {
            Toast.makeText(this@MainActivity, "Settings button tapped", Toast.LENGTH_SHORT).show()
        }
    }
}