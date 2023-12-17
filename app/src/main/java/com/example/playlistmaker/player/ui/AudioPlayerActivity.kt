package com.example.playlistmaker.player.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.player.ui.models.AudioPlayerPlayButtonState
import com.example.playlistmaker.player.ui.models.TrackInfo

class AudioPlayerActivity: AppCompatActivity() {

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
        viewModel.trackInfo.observe(this) {
            configure(it)
        }
        viewModel.audioPlaybackModel.observe(this) {
            updatePlaybackTime(it.playbackTime)
            when (it.playButtonState) {
                AudioPlayerPlayButtonState.PLAY -> showPlayButton()
                AudioPlayerPlayButtonState.PAUSE -> showPauseButton()
                else -> return@observe
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

        with(binding) {
            trackNameTextView.text = model.trackName
            artistNameTextView.text = model.artistName
            songDurationTextView.text = model.trackTime
            if (model.albumName != null) {
                albumContainer.isVisible = true
                albumTextView.text = model.albumName
            } else {
                albumContainer.isVisible = false
            }
            yearTextView.text = model.releaseYear
            genreTextView.text = model.genreName
            countryTextView.text = model.country
        }
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