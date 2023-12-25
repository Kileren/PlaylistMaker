package com.example.playlistmaker.library.ui.liked

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentLikedSongsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LikedSongsFragment: Fragment() {

    private val viewModel by viewModel<LikedSongsViewModel>()
    private var _binding: FragmentLikedSongsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLikedSongsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = LikedSongsFragment()
    }
}