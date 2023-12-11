package com.example.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track

class SearchViewHolder(parentView: ViewGroup): RecyclerView.ViewHolder(
    LayoutInflater.from(parentView.context)
        .inflate(R.layout.search_song_cell_view, parentView, false)
) {

    private val posterImageView: ImageView = itemView.findViewById(R.id.poster_image_view)
    private val songTextView: TextView = itemView.findViewById(R.id.song_name_text_view)
    private val artistTextView: TextView = itemView.findViewById(R.id.artist_name_text_view)
    private val durationTextView: TextView = itemView.findViewById(R.id.song_duration_text_view)

    fun bind(model: Track) {
        val cornerRadius = itemView.context.resources.getDimensionPixelSize(R.dimen.xss_corner_radius)
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .centerCrop()
            .transform(RoundedCorners(cornerRadius))
            .placeholder(R.drawable.image_placeholder)
            .into(posterImageView)

        songTextView.text = model.trackName
        artistTextView.text = model.artistName
        durationTextView.text = model.trackTime
    }
}