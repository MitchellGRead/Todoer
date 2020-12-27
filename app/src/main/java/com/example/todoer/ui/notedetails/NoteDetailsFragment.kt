package com.example.todoer.ui.notedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.todoer.MainActivity
import com.example.todoer.R
import com.example.todoer.daggerhilt.UiScope
import com.example.todoer.databinding.FragmentNoteDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class NoteDetailsFragment : Fragment() {

    private lateinit var binding: FragmentNoteDetailsBinding
    private val args: NoteDetailsFragmentArgs by navArgs()

    @Inject @UiScope lateinit var uiScope: CoroutineScope
    @Inject lateinit var viewModelAssistedInjectFactory: NoteDetailsViewModel.AssistedFactory
    private val viewModel: NoteDetailsViewModel by viewModels {
        NoteDetailsViewModel.provideFactory(viewModelAssistedInjectFactory, args.noteDetailArgs.noteId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("Creating NoteDetails fragment")
        (activity as MainActivity).supportActionBar?.title = args.noteDetailArgs.noteName

        binding = DataBindingUtil.inflate(inflater, LAYOUT_ID, container, false)
        binding.lifecycleOwner = this

        setupView()

        return binding.root
    }

    private fun setupView() {
        with (binding) {
            uiScope.launch {
                val noteDescription = viewModel.getNoteDescription()
                noteContent.setText(noteDescription)
            }
            noteContent.doAfterTextChanged { text ->
                text?.let {
                    viewModel.saveNoteContent(it.toString())
                }
            }
        }
    }

    companion object {
        const val LAYOUT_ID = R.layout.fragment_note_details
    }
}
