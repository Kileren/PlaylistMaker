package com.example.playlistmaker.search.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.player.ui.AudioPlayerActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.search.domain.Track
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {

    private val viewModel by viewModel<SearchViewModel>()
    private lateinit var binding: ActivitySearchBinding

    private val searchAdapter by lazy { SearchAdapter(listOf()) { onTrackTap(it) } }
    private val historyAdapter by lazy { SearchAdapter(listOf()) { onTrackTap(it) } }

    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        configureViews()

        setObservers()
        setListeners()
        setupUI()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onSaveInstanceState(outState, binding.searchTextField.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        viewModel.onRestoreInstanceState(savedInstanceState)
    }

    private fun configureViews() {
        setContentView(binding.root)
        binding.exceptionContainer.isVisible = false
        binding.historyContainer.isVisible = false
    }

    private fun setObservers() {
        viewModel.searchFieldText.observe(this) {
            binding.searchTextField.setText(it)
            binding.searchTextField.setSelection(it.length)
        }
        viewModel.state.observe(this) { state ->
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
        binding.backButton.setOnClickListener { finish() }
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
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = searchAdapter

        // History Recycler View
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
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

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (current) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun setTracks(tracks: List<Track>) {
        searchAdapter.update(tracks)
        if (tracks.isEmpty()) {
            binding.exceptionContainer.isVisible = true
            binding.refreshButton.isVisible = false

            binding.exceptionImageView.setImageDrawable(getDrawable(R.drawable.empty_search_icon))
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

        binding.exceptionImageView.setImageDrawable(getDrawable(R.drawable.error_search_icon))
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

    private fun updateHistoryListIfNeeded(tracks: Array<Track>) {
        if (binding.historyContainer.visibility == View.VISIBLE) {
            historyAdapter.update(tracks.toList())
        }
    }


    private fun configureRefreshButton(searchText: String) {
        binding.refreshButton.setOnClickListener {
            viewModel.tapRefreshButton(searchText)
        }
    }

    private fun onTrackTap(track: Track) {
        if (!clickDebounce()) return

        viewModel.tapOnTrack(track)

        val audioPlayer = Intent(this, AudioPlayerActivity::class.java)
        audioPlayer.putExtra(AudioPlayerActivity.trackKey, track.trackId)
        startActivity(audioPlayer)
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}