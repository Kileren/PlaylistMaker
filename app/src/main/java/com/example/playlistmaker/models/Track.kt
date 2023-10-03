package com.example.playlistmaker.models

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Locale

data class Track(
    val trackId: String, // ID трека
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val artworkUrl100: String?, // Ссылка на изображение обложки
    val country: String, // Страна исполнителя
    @SerializedName("primaryGenreName") val genreName: String, // Жанр трека
    @SerializedName("collectionName") val albumName: String?, // Название альбома
    private val trackTimeMillis: Long, // Продолжительность трека
    val releaseDate: String?, // Дата релиза трека
) {
    fun trackTime(): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
    fun coverArtwork(): String? = artworkUrl100?.replaceAfterLast('/',"512x512bb.jpg")
    fun releaseYear(): String? {
        if (releaseDate == null) return null
        return LocalDateTime
            .parse(releaseDate.removeSuffix("Z")).year
            .toString()
    }
}