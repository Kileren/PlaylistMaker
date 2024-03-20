package com.example.playlistmaker.library.ui.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.library.domain.Playlist
import com.example.playlistmaker.library.ui.playlist.PlaylistFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment: Fragment() {

    private val viewModel by viewModel<PlaylistsViewModel>()
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    private val playlistsAdapter = PlaylistsAdapter(listOf()) { didTapPlaylist(it) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistsBinding.inflate(layoutInflater, container, false)
        setUpUI()
        setObservers()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpUI() {
        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_newPlaylistFragment
            )
        }

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = playlistsAdapter
    }

    private fun setObservers() {
        viewModel.observeState().observe(viewLifecycleOwner) {
            renderState(it)
        }
    }

    private fun renderState(state: PlaylistsState) {
        when (state) {
            is PlaylistsState.Empty -> {
                binding.recyclerView.isVisible = false
                binding.imageView.isVisible = true
                binding.messageTextView.isVisible = true
            }
            is PlaylistsState.Content -> {
                binding.recyclerView.isVisible = true
                binding.imageView.isVisible = false
                binding.messageTextView.isVisible = false

                playlistsAdapter.update(state.playlists)
            }
        }
    }

    private fun didTapPlaylist(playlist: Playlist) {
        findNavController().navigate(
            R.id.action_mediaLibraryFragment_to_playlistFragment,
            bundleOf(PlaylistFragment.playlistKey to playlist.id)
        )
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}