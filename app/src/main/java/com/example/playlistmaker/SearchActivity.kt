package com.example.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.core.view.isVisible

class SearchActivity : AppCompatActivity() {

    companion object {
        private const val SEARCH_VALUE = "SEARCH_VALUE"
    }

    private lateinit var backButton: ImageView
    private lateinit var searchEditText: EditText
    private lateinit var clearImage: ImageView

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
    }

    private fun setListeners() {
        backButton.setOnClickListener { finish() }
        searchEditText.addTextChangedListener(makeSearchTextWatcher())
        clearImage.setOnClickListener {
            searchEditText.text = null
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchEditText.windowToken, 0)
            searchEditText.clearFocus()
        }
    }

    private fun setupUI() {
        clearImage.isVisible = false
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
}