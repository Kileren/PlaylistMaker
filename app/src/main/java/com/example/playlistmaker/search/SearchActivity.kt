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

class SearchActivity : AppCompatActivity() {

    companion object {
        private const val SEARCH_VALUE = "SEARCH_VALUE"
    }

    private val iTunesService = ITunesService()
    private val searchAdapter = SearchAdapter(listOf())

    private lateinit var backButton: ImageView
    private lateinit var searchEditText: EditText
    private lateinit var clearImage: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var exceptionContainer: View
    private lateinit var exceptionIcon: ImageView
    private lateinit var exceptionTextView: TextView
    private lateinit var exceptionRefreshButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViews()
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

    private fun setViews() {
        setContentView(R.layout.activity_search)
        backButton = findViewById(R.id.back_button)
        searchEditText = findViewById(R.id.search_text_field)
        clearImage = findViewById(R.id.clear_image_view)
        recyclerView = findViewById(R.id.recycler_view)
        exceptionContainer = findViewById(R.id.exception_container)
        exceptionIcon = findViewById(R.id.exception_image_view)
        exceptionTextView = findViewById(R.id.exception_text_view)
        exceptionRefreshButton = findViewById(R.id.refresh_button)

        exceptionContainer.visibility = View.GONE
    }

    private fun setListeners() {
        backButton.setOnClickListener { finish() }
        searchEditText.addTextChangedListener(makeSearchTextWatcher())
        searchEditText.setOnEditorActionListener(makeSearchEditorActionListener())
        clearImage.setOnClickListener {
            searchEditText.text = null
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchEditText.windowToken, 0)
            searchEditText.clearFocus()
            searchAdapter.update(listOf())
        }
    }

    private fun setupUI() {
        clearImage.isVisible = false

        // Recycler View
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = searchAdapter
    }

    private fun makeSearchTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearImage.isVisible = !s.isNullOrEmpty()
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
    }

    private fun showNetworkError() {
        searchAdapter.update(listOf())
        exceptionContainer.visibility = View.VISIBLE
        exceptionRefreshButton.visibility = View.VISIBLE

        exceptionIcon.setImageDrawable(getDrawable(R.drawable.error_search_icon))
        exceptionTextView.text = getString(R.string.search_connection_error)
    }

    private fun configureRefreshButton(searchText: String) {
        exceptionRefreshButton.setOnClickListener {
            search(searchText)
        }
    }
}