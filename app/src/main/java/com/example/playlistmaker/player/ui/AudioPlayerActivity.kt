package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.R

class AudioPlayerActivity: ComponentActivity() {

    private lateinit var viewModel: AudioPlayerViewModel

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

        viewModel = ViewModelProvider(
            this,
            AudioPlayerViewModel.getViewModelFactory(
                Creator.createAudioPlayerInteractor(this)
            )
        ).get()

        configureUI()
        setObservers()
        viewModel.onCreate(intent, this)
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    private fun configureUI() {
        playButton.isEnabled = false
        playButton.setOnClickListener { viewModel.playButtonTapped() }
        backButton.setOnClickListener { finish() }
    }

    private fun setObservers() {
        viewModel.getTrackInfo().observe(this) {
            configure(it)
        }
        viewModel.getAudioPlaybackModel().observe(this) {
            updatePlaybackTime(it.playbackTime)
            when (it.playButtonState) {
                AudioPlayerPlayButtonState.PLAY -> showPlayButton()
                AudioPlayerPlayButtonState.PAUSE -> showPauseButton()
            }
        }
    }

    private fun configure(model: TrackInfo) {
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

    private fun showPlayButton() {
        playButton.isEnabled = true
        playButton.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.play_icon))
    }

    private fun showPauseButton() {
        playButton.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.pause_icon))
    }

    private fun updatePlaybackTime(value: String) {
        playbackTimeTextView.text = value
    }

    companion object {
        const val trackKey = "audioPlayer.track"
    }
}