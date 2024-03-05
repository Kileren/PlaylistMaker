package com.example.playlistmaker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.library.domain.Playlist

class AudioPlayerPlaylistsAdapter(
    private var data: List<Playlist>,
    private var onTap: (Playlist) -> Unit
): RecyclerView.Adapter<AudioPlayerPlaylistsViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AudioPlayerPlaylistsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_cell_view, parent, false)
        return AudioPlayerPlaylistsViewHolder(view)
    }

    override fun onBindViewHolder(holder: AudioPlayerPlaylistsViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            onTap(data[position])
        }
    }

    override fun getItemCount(): Int = data.size

    fun update(data: List<Playlist>) {
        this.data = data
        notifyDataSetChanged()
    }
}