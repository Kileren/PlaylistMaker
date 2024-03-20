package com.example.playlistmaker.library.ui.playlists

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.library.domain.Playlist

class PlaylistsViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val imageView: ImageView = itemView.findViewById(R.id.image_view)
    private val titleTextView: TextView = itemView.findViewById(R.id.title_text_view)
    private val descriptionTextView: TextView = itemView.findViewById(R.id.description_text_view)

    fun bind(playlist: Playlist) {
        if (playlist.coverUri != null) {
            imageView.setImageURI(playlist.coverUri)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.clipToOutline = true
            imageView.background = AppCompatResources.getDrawable(itemView.context, R.drawable.rounded_image_view_8dp)
            imageView.scaleX = 1F
            imageView.scaleY = 1F
        } else {
            imageView.setImageResource(R.drawable.image_placeholder)
            imageView.scaleX = 0.85F
            imageView.scaleY = 0.85F
        }

        titleTextView.text = playlist.title
        descriptionTextView.text = itemView.context.resources.getQuantityString(
            R.plurals.number_of_tracks,
            playlist.tracks.size,
            playlist.tracks.size
        )
    }
}