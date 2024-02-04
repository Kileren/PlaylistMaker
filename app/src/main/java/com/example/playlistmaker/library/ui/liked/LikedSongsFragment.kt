package com.example.playlistmaker.library.ui.liked

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.FragmentLikedSongsBinding
import com.example.playlistmaker.player.ui.AudioPlayerActivity
import com.example.playlistmaker.search.domain.Track
import com.example.playlistmaker.search.ui.SearchAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LikedSongsFragment: Fragment() {

    private val viewModel by viewModel<LikedSongsViewModel>()
    private var _binding: FragmentLikedSongsBinding? = null
    private val binding get() = _binding!!

    private val tracksAdapter by lazy { SearchAdapter(listOf()) { onTrackTap(it) } }

    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setUpAndConfigureBindings(inflater, container)
        setObervers()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.updateData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpAndConfigureBindings(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) {
        _binding = FragmentLikedSongsBinding.inflate(inflater, container, false)

        binding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = tracksAdapter
        binding.recyclerView.isVisible = false
    }

    private fun setObervers() {
        viewModel.observeState().observe(viewLifecycleOwner) {
            renderState(it)
        }
    }

    private fun renderState(state: LikedSongsState) {
        when (state) {
            is LikedSongsState.Empty -> {
                binding.recyclerView.isVisible = false
                binding.imageView.isVisible = true
                binding.messageTextView.isVisible = true
            }
            is LikedSongsState.Content -> {
                tracksAdapter.update(state.tracks)
                binding.recyclerView.isVisible = true
                binding.imageView.isVisible = false
                binding.messageTextView.isVisible = false
            }
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (current) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
        }
        return current
    }

    private fun onTrackTap(track: Track) {
        if (!clickDebounce()) return

        val audioPlayer = Intent(context, AudioPlayerActivity::class.java)
        audioPlayer.putExtra(AudioPlayerActivity.trackKey, track.trackId)
        startActivity(audioPlayer)
    }

    companion object {
        fun newInstance() = LikedSongsFragment()

        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
    }
}