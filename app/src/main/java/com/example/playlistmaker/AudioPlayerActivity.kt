package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.models.Track
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayerActivity: AppCompatActivity() {

    companion object {
        const val trackKey = "audioPlayer.track"

        private const val PLAYER_STATE_DEFAULT = 0
        private const val PLAYER_STATE_PREPARED = 1
        private const val PLAYER_STATE_PLAYING = 2
        private const val PLAYER_STATE_PAUSED = 3

        private const val PLAYER_PLAYBACK_REFRESH_DELAY = 300L
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
    private val playButton: ImageButton by lazy { findViewById(R.id.play_button) }
    private val playbackTimeTextView: TextView by lazy { findViewById(R.id.playback_time_text_view) }

    private lateinit var track: Track

    private val handler = Handler(Looper.getMainLooper())
    private val player = MediaPlayer()
    private var playerState = PLAYER_STATE_DEFAULT
    private var playerTimerRunnable = Runnable { refreshPlaybackTime() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTrack()
        setContentView(R.layout.activity_audio_player)
        configureUI()
        configurePlayer()
    }

    override fun onPause() {
        super.onPause()
        if (playerState == PLAYER_STATE_PLAYING) {
            pausePlayer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
        stopPlaybackTimeRefreshing()
    }

    private fun setTrack() {
        val json = intent.getStringExtra(trackKey)
        if (json != null) {
            this.track = Gson().fromJson(json, Track::class.java)
        } else {
            assert(false) { "You should provide track to this activity" }
        }
    }

    private fun configureUI() {
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

        playButton.isEnabled = false
        playButton.setOnClickListener { playbackControl() }
        backButton.setOnClickListener { finish() }
    }

    private fun configurePlayer() {
        if (track.previewUrl == null) return

        player.setDataSource(track.previewUrl)
        player.prepareAsync()
        player.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = PLAYER_STATE_PREPARED
        }
        player.setOnCompletionListener {
            playButton.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.play_icon))
            playerState = PLAYER_STATE_PREPARED
            stopPlaybackTimeRefreshing()
            resetPlaybackTime()
        }
    }

    private fun playbackControl() {
        when (playerState) {
            PLAYER_STATE_PLAYING -> pausePlayer()
            PLAYER_STATE_PREPARED, PLAYER_STATE_PAUSED -> startPlayer()
            else -> return
        }
    }

    private fun startPlayer() {
        player.start()
        playButton.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.pause_icon))
        playerState = PLAYER_STATE_PLAYING
        startPlaybackTimeRefreshing()
    }

    private fun pausePlayer() {
        player.pause()
        playButton.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.play_icon))
        playerState = PLAYER_STATE_PAUSED
        stopPlaybackTimeRefreshing()
    }

    private fun startPlaybackTimeRefreshing() {
        handler.postDelayed(playerTimerRunnable, PLAYER_PLAYBACK_REFRESH_DELAY)
    }

    private fun stopPlaybackTimeRefreshing() {
        handler.removeCallbacks(playerTimerRunnable)
    }

    private fun refreshPlaybackTime() {
        val time = SimpleDateFormat("mm:ss", Locale.getDefault()).format(player.currentPosition)
        playbackTimeTextView.text = time
        startPlaybackTimeRefreshing()
    }

    private fun resetPlaybackTime() {
        playbackTimeTextView.text = getString(R.string.default_audio_playback_time)
    }
}