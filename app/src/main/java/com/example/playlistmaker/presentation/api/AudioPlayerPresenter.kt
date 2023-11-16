package com.example.playlistmaker.presentation.api

import android.content.Context
import android.content.Intent

interface AudioPlayerPresenter {
    fun onCreate(intent: Intent, context: Context)
    fun onPause()
    fun onDestroy()
    fun playButtonTapped()
}