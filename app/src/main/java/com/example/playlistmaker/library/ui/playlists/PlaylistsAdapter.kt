package com.example.playlistmaker.library.ui.playlists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.library.domain.Playlist

class PlaylistsAdapter(
    private var playlists: List<Playlist>
): RecyclerView.Adapter<PlaylistsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_card_view, parent, false)
        return PlaylistsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        holder.bind(playlists[position])
    }

    fun update(playlists: List<Playlist>) {
        this.playlists = playlists
        notifyDataSetChanged()
    }
}