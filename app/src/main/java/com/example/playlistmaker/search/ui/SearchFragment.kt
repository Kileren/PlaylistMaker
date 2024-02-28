package com.example.playlistmaker.search.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.player.ui.AudioPlayerFragment
import com.example.playlistmaker.search.domain.Track
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SearchViewModel>()

    private val searchAdapter by lazy { SearchAdapter(listOf()) { onTrackTap(it) } }
    private val historyAdapter by lazy { SearchAdapter(listOf()) { onTrackTap(it) } }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setUpAndConfigureBindings(inflater, container)
        setObservers()
        setListeners()
        setupUI()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onViewCreated()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onSaveInstanceState(outState, binding.searchTextField.text.toString())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            viewModel.onRestoreInstanceState(savedInstanceState)
        }
    }

    private fun setUpAndConfigureBindings(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.exceptionContainer.isVisible = false
        binding.historyContainer.isVisible = false
    }

    private fun setObservers() {
        viewModel.searchFieldText.observe(viewLifecycleOwner) {
            binding.searchTextField.setText(it)
            binding.searchTextField.setSelection(it.length)
        }
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                SearchState.Loading -> setProgressBarVisible(true)
                is SearchState.Error -> showNetworkError(state)
                is SearchState.History -> showHistory(state)
                is SearchState.Result -> showTracks(state)
                null -> hideHistory()
            }
        }
    }

    private fun setListeners() {
        binding.searchTextField.addTextChangedListener(makeSearchTextWatcher())
        binding.searchTextField.setOnEditorActionListener(makeSearchEditorActionListener())
        binding.searchTextField.setOnFocusChangeListener { _, hasFocus ->
            viewModel.onFocusChanged(binding.searchTextField.text.toString(), hasFocus)
        }
        binding.clearImageView.setOnClickListener {
            viewModel.tapOnClearTextButton()
        }
        binding.clearHistoryButton.setOnClickListener {
            viewModel.tapOnClearHistoryButton()
        }
    }

    private fun setupUI() {
        binding.clearImageView.isVisible = false

        // Recycler View
        binding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = searchAdapter

        // History Recycler View
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.historyRecyclerView.adapter = historyAdapter
    }

    private fun makeSearchTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearImageView.isVisible = !s.isNullOrEmpty()
                viewModel.onSearchTextChanged(s.toString(), binding.searchTextField.hasFocus())
            }
            override fun afterTextChanged(s: Editable?) {}
        }
    }

    private fun makeSearchEditorActionListener(): TextView.OnEditorActionListener {
        return object : TextView.OnEditorActionListener {
            override fun onEditorAction(
                textView: TextView?,
                actionId: Int,
                keyEvent: KeyEvent?
            ): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    viewModel.tapOnSearchButton()
                    return true
                }
                return false
            }
        }
    }

    private fun setTracks(tracks: List<Track>) {
        searchAdapter.update(tracks)
        if (tracks.isEmpty()) {
            binding.exceptionContainer.isVisible = true
            binding.refreshButton.isVisible = false

            val image = requireContext().getDrawable(R.drawable.empty_search_icon)
            binding.exceptionImageView.setImageDrawable(image)
            binding.exceptionTextView.text = getString(R.string.empty_search_text)
        } else {
            binding.exceptionContainer.isVisible = false
        }
        hideHistory()
    }

    private fun setProgressBarVisible(visible: Boolean) {
        if (visible) {
            binding.progressBar.isVisible = true
            binding.recyclerView.isVisible = false
            binding.exceptionContainer.isVisible = false
        } else {
            binding.progressBar.isVisible = false
            binding.recyclerView.isVisible = true
        }
    }

    private fun showTracks(state: SearchState.Result) {
        setProgressBarVisible(false)
        setTracks(state.tracks)
    }

    private fun showNetworkError(state: SearchState.Error) {
        setProgressBarVisible(false)
        configureRefreshButton(state.text)

        searchAdapter.update(listOf())
        binding.exceptionContainer.isVisible = true
        binding.refreshButton.isVisible = true
        hideHistory()

        val image = requireContext().getDrawable(R.drawable.error_search_icon)
        binding.exceptionImageView.setImageDrawable(image)
        binding.exceptionTextView.text = getString(R.string.search_connection_error)
    }

    private fun showHistory(history: SearchState.History) {
        setProgressBarVisible(false)
        searchAdapter.update(listOf())

        val tracks = history.tracks
        if (tracks.isEmpty()) return
        binding.historyContainer.isVisible = true
        binding.exceptionContainer.isVisible = false
        updateHistoryListIfNeeded(tracks)
    }

    private fun hideHistory() {
        binding.historyContainer.isVisible = false
    }

    private fun updateHistoryListIfNeeded(tracks: List<Track>) {
        if (binding.historyContainer.visibility == View.VISIBLE) {
            historyAdapter.update(tracks)
        }
    }


    private fun configureRefreshButton(searchText: String) {
        binding.refreshButton.setOnClickListener {
            viewModel.tapRefreshButton(searchText)
        }
    }

    private fun onTrackTap(track: Track) {
        viewModel.tapOnTrack(track) {
            findNavController().navigate(
                R.id.action_searchFragment_to_audioPlayerFragment,
                bundleOf(AudioPlayerFragment.trackKey to track.trackId)
            )
        }
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}