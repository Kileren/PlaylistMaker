package com.example.playlistmaker.library.ui.newPlaylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPlaylistFragment: Fragment() {

    private val viewModel by viewModel<NewPlaylistViewModel>()
    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding get() = _binding!!

    private var isImageSetted = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setUpAndConfigureBinding(inflater, container)
        setObservers()
        viewModel.onCreateView(this)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpAndConfigureBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) {
        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)

        binding.backButton.setOnClickListener {
            didTapBackButton()
        }
        binding.imageView.setOnClickListener {
            viewModel.selectMedia()
        }
        binding.createButton.setOnClickListener {
            didTapCreateButton()
        }
        binding.titleEditText.doOnTextChanged { text, _, _, _ ->
            binding.titleTextInputLayout.textChanged(text)
            binding.createButton.isEnabled = !text.isNullOrEmpty()
        }
        binding.descriptionEditText.doOnTextChanged { text, _, _, _ ->
            binding.descriptionTextInputLayout.textChanged(text)
        }

        binding.titleTextInputLayout.textChanged(null)
        binding.descriptionTextInputLayout.textChanged(null)
    }

    private fun setObservers() {
        viewModel.observeCover().observe(viewLifecycleOwner) { uri ->
            if (uri != null) else { return@observe }

            binding.imageView.setImageURI(uri)
            binding.imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            binding.imageView.clipToOutline = true
            binding.imageView.background = AppCompatResources.getDrawable(requireContext(), R.drawable.rounded_image_view_8dp)
            isImageSetted = true
        }
    }

    private fun didTapBackButton() {
        val isTitleSetted = binding.titleEditText.text?.isNotEmpty() == true
        val isDescriptionSetted = binding.descriptionEditText.text?.isNotEmpty() == true

        if (isTitleSetted || isDescriptionSetted || isImageSetted) {
            showExitConfirmationDialog()
        } else {
            close()
        }
    }

    private fun didTapCreateButton() {
        val title = binding.titleEditText.text.toString() ?: ""
        val description = binding.descriptionEditText.text.toString()

        viewModel.createPlaylist(
            title = title,
            description = description
        )
        close()

        Toast.makeText(
            requireContext(),
            requireContext().getString(R.string.playlist_created_message, title),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showExitConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(requireContext().getString(R.string.new_playlist_exit_dialog_title))
            .setMessage(requireContext().getString(R.string.new_playlist_exit_dialog_message))
            .setNeutralButton(requireContext().getString(R.string.cancel)) { dialog, _ ->
                dialog.cancel()
            }
            .setNegativeButton(requireContext().getString(R.string.complete)) { _, _ ->
                close()
            }
            .show()
    }

    private fun close() {
        findNavController().navigateUp()
    }
}

private fun TextInputLayout.textChanged(text: CharSequence?) {
    if (text.isNullOrEmpty()) {
        defaultHintTextColor = resources.getColorStateList(R.color.dynamic_text_primary, null)
        setBoxStrokeColorStateList(resources.getColorStateList(R.color.text_input_box_stroke_color, null))
    } else {
        defaultHintTextColor = resources.getColorStateList(R.color.text_input_with_content_box_stroke_color, null)
        setBoxStrokeColorStateList(resources.getColorStateList(R.color.text_input_with_content_box_stroke_color, null))
    }
}