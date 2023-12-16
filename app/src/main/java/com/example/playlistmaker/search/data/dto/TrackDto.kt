package com.example.playlistmaker.search.data.dto

import com.google.gson.annotations.SerializedName

data class TrackDto(
    val trackId: String?, // ID трека
    val trackName: String?, // Название композиции
    val artistName: String?, // Имя исполнителя
    val artworkUrl100: String?, // Ссылка на изображение обложки
    val country: String?, // Страна исполнителя
    val releaseDate: String?, // Дата релиза трека
    val previewUrl: String?, // Ссылка на часть трека
    val trackTimeMillis: Long?, // Продолжительность трека
    @SerializedName("primaryGenreName") val genreName: String?, // Жанр трека
    @SerializedName("collectionName") val albumName: String?, // Название альбома
)