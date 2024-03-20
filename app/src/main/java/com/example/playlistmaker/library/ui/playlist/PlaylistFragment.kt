package com.example.playlistmaker.library.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.library.ui.newPlaylist.NewPlaylistFragment
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
    private var playlistEditAction: () -> Unit = {}

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
        binding.shareImageView.setOnClickListener { didTapShareButton() }
        binding.menuImageView.setOnClickListener { didTapMenu() }
        binding.backButton.setOnClickListener { didTapBackButton() }

        binding.menuShareTextView.setOnClickListener { didTapShareButton() }
        binding.menuEditInformationTextView.setOnClickListener { playlistEditAction() }
        binding.menuDeletePlaylistTextView.setOnClickListener { didTapDeletePlaylist() }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.menuBottomSheet)
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

        binding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = tracksAdapter

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            didTapBackButton()
        }
    }

    private fun setObservers() {
        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            renderState(state)

            if (state is PlaylistState.Content) {
                playlistEditAction = {
                    findNavController().navigate(
                        R.id.action_playlistFragment_to_newPlaylistFragment,
                        bundleOf(
                            NewPlaylistFragment.editPlaylistId to requireArguments().getInt(playlistKey).toString(),
                            NewPlaylistFragment.editImageKey to state.coverUri?.toString(),
                            NewPlaylistFragment.editTitleKey to state.title,
                            NewPlaylistFragment.editDescriptionKey to state.description
                        )
                    )
                }
            }
        }
    }

    private fun renderState(state: PlaylistState) {
        when (state) {
            is PlaylistState.Removed -> {
                findNavController().navigateUp()
            }
            is PlaylistState.Content -> {
                if (state.coverUri != null) {
                    binding.imageView.setImageURI(state.coverUri)
                    binding.imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                    binding.imageView.scaleX = 1f
                    binding.imageView.scaleY = 1f

                    binding.menuPlaylistImageView.setImageURI(state.coverUri)
                    binding.menuPlaylistImageView.scaleType = ImageView.ScaleType.CENTER_CROP
                    binding.menuPlaylistImageView.scaleX = 1f
                    binding.menuPlaylistImageView.scaleY = 1f
                    binding.menuPlaylistImageView.clipToOutline = true
                    binding.menuPlaylistImageView.background = AppCompatResources.getDrawable(requireContext(), R.drawable.rounded_image_view_2dp)
                } else {
                    binding.imageView.setImageResource(R.drawable.image_placeholder)
                    binding.imageView.scaleX = 0.8f
                    binding.imageView.scaleY = 0.8f

                    binding.menuPlaylistImageView.setImageResource(R.drawable.image_placeholder)
                    binding.menuPlaylistImageView.scaleX = 0.8f
                    binding.menuPlaylistImageView.scaleY = 0.8f
                }
                binding.titleTextView.text = state.title
                binding.menuPlaylistTitleTextView.text = state.title

                if (state.description != null) {
                    binding.subtitleTextView.text = state.description
                    binding.subtitleTextView.isVisible = true
                } else {
                    binding.subtitleTextView.isVisible = false
                }

                binding.additionalInfoTextView.text = requireContext().getString(
                    R.string.playlist_additional_info,
                    state.totalDuration,
                    state.totalTracks
                )
                binding.menuPlaylistSubtitleTextView.text = state.totalTracks

                tracksAdapter.update(state.tracks)
                binding.emptyTracksMessage.isVisible = state.tracks.isEmpty()
                binding.recyclerView.isVisible = state.tracks.isNotEmpty()
            }
        }
    }

    private fun didTapShareButton() {
        BottomSheetBehavior.from(binding.menuBottomSheet).state = BottomSheetBehavior.STATE_HIDDEN
        if (tracksAdapter.itemCount == 0) {
            Toast.makeText(
                requireContext(),
                R.string.empty_playlist_share_message,
                Toast.LENGTH_SHORT
            ).show()
        } else {
            viewModel.sharePlaylist(requireContext())
        }
    }

    private fun didTapMenu() {
        BottomSheetBehavior.from(binding.menuBottomSheet).state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun didTapDeletePlaylist() {
        BottomSheetBehavior.from(binding.menuBottomSheet).state = BottomSheetBehavior.STATE_HIDDEN

        MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialogTheme)
            .setTitle(R.string.delete_playlist)
            .setMessage(R.string.delete_playlist_confirmation_message)
            .setNegativeButton(requireContext().getString(R.string.no)) { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton(requireContext().getString(R.string.yes)) { _, _ ->
                viewModel.deletePlaylist()
            }
            .show()
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
        MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialogTheme)
            .setTitle(requireContext().getString(R.string.remove_track_title))
            .setMessage(requireContext().getString(R.string.remove_track_description))
            .setNegativeButton(requireContext().getString(R.string.no)) { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton(requireContext().getString(R.string.yes)) { _, _ ->
                viewModel.removeTrack(track, requireContext())
            }
            .show()
    }

    companion object {
        const val playlistKey = "playlist.key"
    }
}