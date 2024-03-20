package com.example.playlistmaker.library.ui.playlist

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.search.domain.Track
import com.example.playlistmaker.search.ui.SearchViewHolder

class PlaylistTrackAdapter(
    private var data: List<Track>,
    private var onTap: (Track) -> Unit,
    private var onLongTap: (Track) -> Unit
): RecyclerView.Adapter<SearchViewHolder>()  {

    private lateinit var viewHolder: SearchViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        viewHolder = SearchViewHolder(parent)
        return viewHolder
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            onTap(data[position])
        }
        holder.itemView.setOnLongClickListener {
            onLongTap(data[position])
            true
        }
    }

    override fun getItemCount(): Int = data.size

    fun update(data: List<Track>) {
        this.data = data
        notifyDataSetChanged()
    }
}