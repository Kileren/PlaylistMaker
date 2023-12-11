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
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.presentation.ui.audio_player.AudioPlayerActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.api.AudioPlayer

class SearchActivity : ComponentActivity() {

    private lateinit var viewModel: SearchViewModel

    private val searchAdapter by lazy { SearchAdapter(listOf()) { onTrackTap(it) } }
    private val historyAdapter by lazy { SearchAdapter(listOf()) { onTrackTap(it) } }

    private val backButton: ImageView by lazy { findViewById(R.id.back_button) }
    private val searchEditText: EditText by lazy { findViewById(R.id.search_text_field) }
    private val clearImage: ImageView by lazy { findViewById(R.id.clear_image_view) }
    private val recyclerView: RecyclerView by lazy { findViewById(R.id.recycler_view) }
    private val exceptionContainer: View by lazy { findViewById(R.id.exception_container) }
    private val exceptionIcon: ImageView by lazy { findViewById(R.id.exception_image_view) }
    private val exceptionTextView: TextView by lazy { findViewById(R.id.exception_text_view) }
    private val exceptionRefreshButton: Button by lazy { findViewById(R.id.refresh_button) }
    private val historyContainer: View by lazy { findViewById(R.id.history_container) }
    private val historyRecyclerView: RecyclerView by lazy { findViewById(R.id.history_recycler_view) }
    private val clearHistoryButton: Button by lazy { findViewById(R.id.clear_history_button) }
    private val progressBar: ProgressBar by lazy { findViewById(R.id.progress_bar) }

    private val handler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureViews()
        viewModel = ViewModelProvider(
            this,
            SearchViewModel.getViewModelFactory(
                Creator.createSearchInteractor(this)
            )
        ).get()

        setObservers()
        setListeners()
        setupUI()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onSaveInstanceState(outState, searchEditText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        viewModel.onRestoreInstanceState(savedInstanceState)
    }

    private fun configureViews() {
        setContentView(R.layout.activity_search)
        exceptionContainer.visibility = View.GONE
        historyContainer.visibility = View.GONE
    }

    private fun setObservers() {
        viewModel.getSearchFieldText().observe(this) {
            searchEditText.setText(it)
            searchEditText.setSelection(it.length)
        }
        viewModel.getState().observe(this) { state ->
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
        backButton.setOnClickListener { finish() }
        searchEditText.addTextChangedListener(makeSearchTextWatcher())
        searchEditText.setOnEditorActionListener(makeSearchEditorActionListener())
        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            viewModel.onFocusChanged(searchEditText.text.toString(), hasFocus)
        }
        clearImage.setOnClickListener {
            viewModel.tapOnClearTextButton()
        }
        clearHistoryButton.setOnClickListener {
            viewModel.tapOnClearHistoryButton()
        }
    }

    private fun setupUI() {
        clearImage.isVisible = false

        // Recycler View
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = searchAdapter

        // History Recycler View
        historyRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        historyRecyclerView.adapter = historyAdapter
    }

    private fun makeSearchTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearImage.isVisible = !s.isNullOrEmpty()
                viewModel.onSearchTextChanged(s.toString(), searchEditText.hasFocus())
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
            exceptionContainer.visibility = View.VISIBLE
            exceptionRefreshButton.visibility = View.GONE

            exceptionIcon.setImageDrawable(getDrawable(R.drawable.empty_search_icon))
            exceptionTextView.text = getString(R.string.empty_search_text)
        } else {
            exceptionContainer.visibility = View.GONE
        }
        hideHistory()
    }

    private fun setProgressBarVisible(visible: Boolean) {
        if (visible) {
            progressBar.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            exceptionContainer.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
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
        exceptionContainer.visibility = View.VISIBLE
        exceptionRefreshButton.visibility = View.VISIBLE
        hideHistory()

        exceptionIcon.setImageDrawable(getDrawable(R.drawable.error_search_icon))
        exceptionTextView.text = getString(R.string.search_connection_error)
    }

    private fun showHistory(history: SearchState.History) {
        setProgressBarVisible(false)
        searchAdapter.update(listOf())

        val tracks = history.tracks
        if (tracks.isEmpty()) return
        historyContainer.visibility = View.VISIBLE
        exceptionContainer.visibility = View.GONE
        updateHistoryListIfNeeded(tracks)
    }

    private fun hideHistory() {
        historyContainer.visibility = View.GONE
    }

    private fun updateHistoryListIfNeeded(tracks: Array<Track>) {
        if (historyContainer.visibility == View.VISIBLE) {
            historyAdapter.update(tracks.toList())
        }
    }


    private fun configureRefreshButton(searchText: String) {
        exceptionRefreshButton.setOnClickListener {
            viewModel.tapRefreshButton(searchText)
        }
    }

    private fun onTrackTap(track: Track) {
        if (!clickDebounce()) return

        viewModel.tapOnTrack(track)

        val audioPlayer = Intent(this, AudioPlayerActivity::class.java)
        audioPlayer.putExtra(AudioPlayer.trackKey, track.trackId)
        startActivity(audioPlayer)
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}