package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.example.playlistmaker.player.ui.models.AddingToPlaylistResult
import com.example.playlistmaker.player.ui.models.AudioPlayerPlayButtonState
import com.example.playlistmaker.player.ui.models.TrackInfo
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel

class AudioPlayerFragment: Fragment() {

    private val viewModel by viewModel<AudioPlayerViewModel>()

    private var _binding: FragmentAudioPlayerBinding? = null
    private val binding get() = _binding!!

    private val playlistsAdapter by lazy { AudioPlayerPlaylistsAdapter(listOf()) { viewModel.addTrackToPlaylist(it) } }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAudioPlayerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureUI()
        setObservers()
        viewModel.onCreate(this, requireContext())
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
        _binding = null
    }

    private fun configureUI() {
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> binding.dimOverlay.isVisible = false
                    else -> binding.dimOverlay.isVisible = true
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = playlistsAdapter

        binding.playButton.isEnabled = false
        binding.playButton.setOnClickListener { viewModel.playButtonTapped() }
        binding.backButton.setOnClickListener { findNavController().navigateUp() }
        binding.likeButton.setOnClickListener { viewModel.favouriteButtonTapped() }
        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_audioPlayerFragment_to_newPlaylistFragment)
        }
        binding.playlistButton.setOnClickListener {
            viewModel.addToPlaylistButtonTapped()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        binding.dimOverlay.setOnClickListener {
            BottomSheetBehavior.from(binding.bottomSheet).state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun setObservers() {
        viewModel.trackInfo.observe(viewLifecycleOwner) {
            configure(it)
        }
        viewModel.audioPlaybackModel.observe(viewLifecycleOwner) {
            updatePlaybackTime(it.playbackTime)
            when (it.playButtonState) {
                AudioPlayerPlayButtonState.PLAY -> showPlayButton()
                AudioPlayerPlayButtonState.PAUSE -> showPauseButton()
                else -> return@observe
            }
        }
        viewModel.observePlaylists.observe(viewLifecycleOwner) {
            playlistsAdapter.update(it)
        }
        viewModel.observeAddingToPlaylist.observe(viewLifecycleOwner) {
            val message = when (it) {
                is AddingToPlaylistResult.Added -> requireContext().getString(R.string.track_added_to_playlist, it.playlistName)
                is AddingToPlaylistResult.AlreadyExists -> requireContext().getString(R.string.track_already_added, it.playlistName)
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

            if (it is AddingToPlaylistResult.Added) {
                BottomSheetBehavior.from(binding.bottomSheet).state = BottomSheetBehavior.STATE_HIDDEN
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
            if (model.isFavourite) {
                likeButton.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.like_icon_on))
            } else {
                likeButton.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.like_icon))
            }
        }
    }

    private fun showPlayButton() {
        binding.playButton.isEnabled = true
        binding.playButton.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.play_icon))
    }

    private fun showPauseButton() {
        binding.playButton.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.pause_icon))
    }

    private fun updatePlaybackTime(value: String) {
        binding.playbackTimeTextView.text = value
    }

    companion object {
        const val trackKey = "audioPlayer.track"
    }
}