package com.example.playlistmaker.search

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.models.Track
import com.example.playlistmaker.network.services.ITunesService
import com.example.playlistmaker.storage.SharedPreferencesStorage
import com.example.playlistmaker.storage.addTrackToHistory

class SearchActivity : AppCompatActivity() {

    companion object {
        private const val SEARCH_VALUE = "SEARCH_VALUE"
    }

    private val sharedPreferencesStorage by lazy { SharedPreferencesStorage(this) }
    private val iTunesService by lazy { ITunesService() }
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureViews()
        setListeners()
        setupUI()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_VALUE, searchEditText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val searchText = savedInstanceState.getString(SEARCH_VALUE)
        if (searchText != null) {
            searchEditText.setText(searchText)
            searchEditText.setSelection(searchText.length)
        }
    }

    private fun configureViews() {
        setContentView(R.layout.activity_search)
        exceptionContainer.visibility = View.GONE
        historyContainer.visibility = View.GONE
    }

    private fun setListeners() {
        backButton.setOnClickListener { finish() }
        searchEditText.addTextChangedListener(makeSearchTextWatcher())
        searchEditText.setOnEditorActionListener(makeSearchEditorActionListener())
        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && searchEditText.text.isEmpty()) {
                showHistoryIfNeeded()
            } else {
                hideHistory()
            }
        }
        clearImage.setOnClickListener {
            searchEditText.text = null
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchEditText.windowToken, 0)
            searchEditText.clearFocus()
            searchAdapter.update(listOf())
        }
        clearHistoryButton.setOnClickListener {
            sharedPreferencesStorage.searchHistory = arrayOf()
            hideHistory()
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
                if (searchEditText.hasFocus() && s?.isEmpty() == true) {
                    showHistoryIfNeeded()
                } else {
                    hideHistory()
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        }
    }

    private fun makeSearchEditorActionListener(): TextView.OnEditorActionListener {
        return object: TextView.OnEditorActionListener {
            override fun onEditorAction(textView: TextView?, actionId: Int, keyEvent: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    search(searchEditText.text.toString())
                    return true
                }
                return false
            }
        }
    }

    private fun search(text: String) {
        iTunesService.search(
            text = text,
            onSuccess = { setTracks(it) },
            onError = {
                configureRefreshButton(text)
                showNetworkError()
            }
        )
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

    private fun showNetworkError() {
        searchAdapter.update(listOf())
        exceptionContainer.visibility = View.VISIBLE
        exceptionRefreshButton.visibility = View.VISIBLE
        hideHistory()

        exceptionIcon.setImageDrawable(getDrawable(R.drawable.error_search_icon))
        exceptionTextView.text = getString(R.string.search_connection_error)
    }

    private fun showHistoryIfNeeded() {
        val tracks = sharedPreferencesStorage.searchHistory
        if (tracks.isEmpty()) return
        historyContainer.visibility = View.VISIBLE
        exceptionContainer.visibility = View.GONE
        updateHistoryListIfNeeded()
    }

    private fun hideHistory() {
        historyContainer.visibility = View.GONE
    }

    private fun updateHistoryListIfNeeded() {
        if (historyContainer.visibility == View.VISIBLE) {
            historyAdapter.update(sharedPreferencesStorage.searchHistory.toList())
        }
    }

    private fun configureRefreshButton(searchText: String) {
        exceptionRefreshButton.setOnClickListener {
            search(searchText)
        }
    }

    private fun onTrackTap(track: Track) {
        sharedPreferencesStorage.addTrackToHistory(track)
        updateHistoryListIfNeeded()
    }
}