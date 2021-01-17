package com.example.todoer.ui.notedetails

import android.os.Bundle
import android.view.*
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.todoer.MainActivity
import com.example.todoer.R
import com.example.todoer.base.BaseFragment
import com.example.todoer.base.ViewModelAction
import com.example.todoer.daggerhilt.UiScope
import com.example.todoer.databinding.FragmentNoteDetailsBinding
import com.example.todoer.sharing.ShareIntentFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class NoteDetailsFragment : BaseFragment() {

    private lateinit var binding: FragmentNoteDetailsBinding
    private val args: NoteDetailsFragmentArgs by navArgs()

    @Inject lateinit var shareIntentFactory: ShareIntentFactory
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
        Timber.d("Creating NoteDetailsFragment")
        (activity as MainActivity).supportActionBar?.title = args.noteDetailArgs.noteName

        binding = DataBindingUtil.inflate(inflater, LAYOUT_ID, container, false)
        binding.lifecycleOwner = this

        setHasOptionsMenu(true)

        viewModel.action.observe(viewLifecycleOwner, Observer { action ->
            action?.let { onAction(it) }
        })

        return binding.root
    }

    private fun onAction(action: ViewModelAction<NoteAction>) {
        val noteAction = action.getContentIfNotHandled()
        when (noteAction) {
            is ShareNote -> {
                val shareIntent = shareIntentFactory.createTextShareIntent(noteAction.data)
                startActivity(shareIntent)
            }
            null -> Timber.e("Error: $action has a null listAction type.")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.note_details_toolbar_options, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.share_todo -> {
                viewModel.shareTodo()
                true
            }
            else -> false
        }
    }

    override fun onResume() {
        super.onResume()
        setupView()
    }

    private fun setupView() {
        with (binding) {
            uiScope.launch {
                val noteDescription = viewModel.getNoteDescription()
                noteContent.setText(noteDescription)
                noteContent.doAfterTextChanged { text ->
                    text?.let {
                        viewModel.saveNoteDescription(it.toString())
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.updateEditedDate()
        uiScope.cancel()
    }

    companion object {
        const val LAYOUT_ID = R.layout.fragment_note_details
    }
}
