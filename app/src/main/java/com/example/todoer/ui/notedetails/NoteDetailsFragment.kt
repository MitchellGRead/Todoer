package com.example.todoer.ui.notedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.todoer.R
import com.example.todoer.databinding.FragmentNoteDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class NoteDetailsFragment : Fragment() {

    private lateinit var binding: FragmentNoteDetailsBinding

    @Inject lateinit var viewModelAssistedInjectFactory: NoteDetailsViewModel.AssistedFactory
    private val viewModel: NoteDetailsViewModel by viewModels {
        NoteDetailsViewModel.provideFactory(viewModelAssistedInjectFactory, 3)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("Creating NoteDetails fragment")

        binding = DataBindingUtil.inflate(inflater, LAYOUT_ID, container, false)
        binding.lifecycleOwner = this

        setupView()

        return binding.root
    }

    private fun setupView() {
        binding.noteContent.setText(viewModel.observeNoteContent())
    }

    companion object {
        const val LAYOUT_ID = R.layout.fragment_note_details
    }
}
