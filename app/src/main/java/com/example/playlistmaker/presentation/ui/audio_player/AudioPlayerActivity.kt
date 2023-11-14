package com.example.playlistmaker.presentation.ui.audio_player

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.api.AudioPlayer
import com.example.playlistmaker.presentation.ui.models.TrackInfo

class AudioPlayerActivity: AppCompatActivity(), AudioPlayer {

    private val presenter = Creator.createAudioPlayerPresenter(this, this)

    private val backButton: ImageView by lazy { findViewById(R.id.back_button) }
    private val imageView: ImageView by lazy { findViewById(R.id.poster_image_view) }
    private val trackTextView: TextView by lazy { findViewById(R.id.track_name_text_view) }
    private val artistTextView: TextView by lazy { findViewById(R.id.artist_name_text_view) }
    private val durationTextView: TextView by lazy { findViewById(R.id.song_duration_text_view) }
    private val albumTextView: TextView by lazy { findViewById(R.id.album_text_view) }
    private val yearTextView: TextView by lazy { findViewById(R.id.year_text_view) }
    private val genreTextView: TextView by lazy { findViewById(R.id.genre_text_view) }
    private val countryTextView: TextView by lazy { findViewById(R.id.country_text_view) }
    private val albumContainerView: View by lazy { findViewById(R.id.album_container) }
    private val playButton: ImageButton by lazy { findViewById(R.id.play_button) }
    private val playbackTimeTextView: TextView by lazy { findViewById(R.id.playback_time_text_view) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        configureUI()
        presenter.onCreate(intent, this)
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    private fun configureUI() {
        playButton.isEnabled = false
        playButton.setOnClickListener { presenter.playButtonTapped() }
        backButton.setOnClickListener { finish() }
    }

    override fun configure(model: TrackInfo) {
        val cornerRadius = resources.getDimensionPixelSize(R.dimen.s_corner_radius)
        Glide.with(this)
            .load(model.coverArtwork)
            .transform(CenterCrop(), RoundedCorners(cornerRadius))
            .placeholder(R.drawable.image_placeholder)
            .into(imageView)

        trackTextView.text = model.trackName
        artistTextView.text = model.artistName
        durationTextView.text = model.trackTime
        if (model.albumName != null) {
            albumContainerView.visibility = View.VISIBLE
            albumTextView.text = model.albumName
        } else {
            albumContainerView.visibility = View.GONE
        }
        yearTextView.text = model.releaseYear
        genreTextView.text = model.genreName
        countryTextView.text = model.country
    }

    override fun showPlayButton() {
        playButton.isEnabled = true
        playButton.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.play_icon))
    }

    override fun showPauseButton() {
        playButton.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.pause_icon))
    }

    override fun updatePlaybackTime(value: String) {
        playbackTimeTextView.text = value
    }
}