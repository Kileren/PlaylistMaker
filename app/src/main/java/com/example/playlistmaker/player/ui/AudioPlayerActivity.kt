package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding

class AudioPlayerActivity: ComponentActivity() {

    private lateinit var viewModel: AudioPlayerViewModel
    private lateinit var binding: ActivityAudioPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        binding.playButton.isEnabled = false
        binding.playButton.setOnClickListener { viewModel.playButtonTapped() }
        binding.backButton.setOnClickListener { finish() }
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
            .into(binding.posterImageView)

        binding.trackNameTextView.text = model.trackName
        binding.artistNameTextView.text = model.artistName
        binding.songDurationTextView.text = model.trackTime
        if (model.albumName != null) {
            binding.albumContainer.visibility = View.VISIBLE
            binding.albumTextView.text = model.albumName
        } else {
            binding.albumContainer.visibility = View.GONE
        }
        binding.yearTextView.text = model.releaseYear
        binding.genreTextView.text = model.genreName
        binding.countryTextView.text = model.country
    }

    private fun showPlayButton() {
        binding.playButton.isEnabled = true
        binding.playButton.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.play_icon))
    }

    private fun showPauseButton() {
        binding.playButton.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.pause_icon))
    }

    private fun updatePlaybackTime(value: String) {
        binding.playbackTimeTextView.text = value
    }

    companion object {
        const val trackKey = "audioPlayer.track"
    }
}