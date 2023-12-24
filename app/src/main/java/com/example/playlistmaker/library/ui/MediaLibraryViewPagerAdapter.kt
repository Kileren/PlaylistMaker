package com.example.playlistmaker.library.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.library.ui.liked.LikedSongsFragment
import com.example.playlistmaker.library.ui.playlists.PlaylistsFragment

class MediaLibraryViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
): FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> LikedSongsFragment.newInstance()
            1 -> PlaylistsFragment.newInstance()
            else -> {
                assert(false) { "Not implemented yet" }
                return LikedSongsFragment.newInstance()
            }
        }
    }
}