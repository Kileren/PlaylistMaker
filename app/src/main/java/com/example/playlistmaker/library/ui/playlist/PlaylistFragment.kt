package com.example.playlistmaker.library.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.player.ui.AudioPlayerFragment
import com.example.playlistmaker.search.domain.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment: Fragment() {

    private val viewModel by viewModel<PlaylistViewModel>()
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private val tracksAdapter by lazy { PlaylistTrackAdapter(
        listOf(),
        onTap = { didTapTrack(it) },
        onLongTap = { didTapTrackLong(it) }
    )}

    private var globalLayoutListener: OnGlobalLayoutListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistBinding.inflate(layoutInflater, container, false)
        setUpUI()
        setObservers()
        viewModel.onCreate(this, requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        globalLayoutListener = OnGlobalLayoutListener {
            val bottomSheetTopPadding = requireContext().resources.getDimension(R.dimen.size_24)
            BottomSheetBehavior.from(binding.bottomSheet).peekHeight =
                view.height - binding.infoContainerLinearLayout.bottom - bottomSheetTopPadding.toInt()
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
    }

    override fun onStop() {
        super.onStop()
        view?.viewTreeObserver?.removeOnGlobalLayoutListener(globalLayoutListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpUI() {
        binding.backButton.setOnClickListener { didTapBackButton() }

        binding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = tracksAdapter

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            didTapBackButton()
        }
    }

    private fun setObservers() {
        viewModel.observeState().observe(viewLifecycleOwner) {
            renderState(it)
        }
    }

    private fun renderState(state: PlaylistState) {
        when (state) {
            is PlaylistState.Removed -> {
                println("")
            }
            is PlaylistState.Content -> {
                if (state.coverUri != null) {
                    binding.imageView.setImageURI(state.coverUri)
                    binding.imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                    binding.imageView.scaleX = 1f
                    binding.imageView.scaleY = 1f
                } else {
                    binding.imageView.setImageResource(R.drawable.image_placeholder)
                    binding.imageView.scaleX = 0.8f
                    binding.imageView.scaleY = 0.8f
                }
                binding.titleTextView.text = state.title

                if (state.description != null) {
                    binding.subtitleTextView.text = state.description
                } else {
                    binding.subtitleTextView.isVisible = false
                }

                binding.additionalInfoTextView.text = state.additionalInfoText

                tracksAdapter.update(state.tracks)
            }
        }
    }

    private fun didTapBackButton() {
        findNavController().navigateUp()
    }

    private fun didTapTrack(track: Track) {
        findNavController().navigate(
            R.id.action_playlistFragment_to_audioPlayerFragment,
            bundleOf(AudioPlayerFragment.trackKey to track.trackId)
        )
    }

    private fun didTapTrackLong(track: Track) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(requireContext().getString(R.string.remove_track_title))
            .setMessage(requireContext().getString(R.string.remove_track_description))
            .setNeutralButton(requireContext().getString(R.string.cancel)) { dialog, _ ->
                dialog.cancel()
            }
            .setNegativeButton(requireContext().getString(R.string.remove)) { _, _ ->
                viewModel.removeTrack(track, requireContext())
            }
            .show()
    }

    companion object {
        const val playlistKey = "playlist.key"
    }
}