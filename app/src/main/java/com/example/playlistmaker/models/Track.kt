package com.example.playlistmaker.models

import java.text.SimpleDateFormat
import java.util.Locale

data class Track(
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val artworkUrl100: String?, // Ссылка на изображение обложки
    private val trackTimeMillis: Long, // Продолжительность трека
) {
    fun trackTime(): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
}