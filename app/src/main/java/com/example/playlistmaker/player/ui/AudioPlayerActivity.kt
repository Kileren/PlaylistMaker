package com.example.playlistmaker.player.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.player.ui.models.AudioPlayerPlayButtonState
import com.example.playlistmaker.player.ui.models.TrackInfo
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel

class AudioPlayerActivity: AppCompatActivity() {

    private val viewModel by viewModel<AudioPlayerViewModel>()
    private lateinit var binding: ActivityAudioPlayerBinding

    private val playlistsAdapter by lazy { AudioPlayerPlaylistsAdapter(listOf()) {  } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = playlistsAdapter

        binding.playButton.isEnabled = false
        binding.playButton.setOnClickListener { viewModel.playButtonTapped() }
        binding.backButton.setOnClickListener { finish() }
        binding.likeButton.setOnClickListener { viewModel.favouriteButtonTapped() }
        binding.playlistButton.setOnClickListener {
            viewModel.addToPlaylistButtonTapped()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.dimOverlay.isVisible = true
        }
        binding.dimOverlay.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            binding.dimOverlay.isVisible = false
        }
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
        viewModel.observePlaylists.observe(this) {
            playlistsAdapter.update(it)
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
            if (model.isFavourite) {
                likeButton.setImageDrawable(getDrawable(R.drawable.like_icon_on))
            } else {
                likeButton.setImageDrawable(getDrawable(R.drawable.like_icon))
            }
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