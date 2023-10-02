package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.models.Track
import com.google.gson.Gson

class AudioPlayerActivity: AppCompatActivity() {

    companion object {
        const val trackKey = "audioPlayer.track"
    }

    private val backButton: ImageView by lazy { findViewById(R.id.back_button) }
    private val imageView: ImageView by lazy { findViewById(R.id.poster_image_view) }
    private val trackTextView: TextView by lazy { findViewById(R.id.track_name_text_view) }
    private val artistTextView: TextView by lazy { findViewById(R.id.artist_name_text_view) }
    private val durationTextView: TextView by lazy { findViewById(R.id.song_duration_text_view) }
    private val albumTextView: TextView by lazy { findViewById(R.id.album_text_view) }
    private val yearTextView: TextView by lazy { findViewById(R.id.year_text_view) }
    private val genreTextView: TextView by lazy { findViewById(R.id.genre_text_view) }
    private val countryTextView: TextView by lazy { findViewById(R.id.country_text_view) }
    private val albumContainerView: View by lazy { findViewById(R.id.album_container) }

    private lateinit var track: Track

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTrack()
        setContentView(R.layout.activity_audio_player)
        configure()
    }

    private fun setTrack() {
        val json = intent.getStringExtra(trackKey)
        if (json != null) {
            this.track = Gson().fromJson(json, Track::class.java)
        } else {
            assert(false) { "You should provide track to this activity" }
        }
    }

    private fun configure() {
        val cornerRadius = resources.getDimensionPixelSize(R.dimen.s_corner_radius)
        Glide.with(this)
            .load(track.coverArtwork())
            .transform(CenterCrop(), RoundedCorners(cornerRadius))
            .placeholder(R.drawable.image_placeholder)
            .into(imageView)

        trackTextView.text = track.trackName
        artistTextView.text = track.artistName
        durationTextView.text = track.trackTime()
        if (track.albumName != null) {
            albumTextView.text = track.albumName
        } else {
            albumContainerView.visibility = View.GONE
        }
        yearTextView.text = track.releaseYear()
        genreTextView.text = track.genreName
        countryTextView.text = track.country

        backButton.setOnClickListener { finish() }
    }
}