package com.example.todoer.ui.listdetails

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.todoer.MainActivity
import com.example.todoer.R
import com.example.todoer.base.BaseFragment
import com.example.todoer.databinding.FragmentListDetailsBinding
import com.example.todoer.ui.listdetails.recycler.ListDetailsAdapter
import com.example.todoer.ui.listdetails.recycler.TodoItemListeners
import com.example.todoer.utils.ViewUtils.setMultiLineAndDoneAction
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ListDetailsFragment : BaseFragment() {

    private lateinit var binding: FragmentListDetailsBinding
    private val args: ListDetailsFragmentArgs by navArgs()
    private lateinit var undoSnackbar: Snackbar

    @Inject lateinit var viewModelAssistedInjectFactory: ListDetailsViewModel.AssistedFactory
    private val viewModel: ListDetailsViewModel by viewModels {
        ListDetailsViewModel.provideFactory(viewModelAssistedInjectFactory, args.listDetailArgs.listId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.d("Creating ListDetails fragment")
        (activity as MainActivity).supportActionBar?.title = args.listDetailArgs.listName

        binding = DataBindingUtil.inflate(inflater, LAYOUT_ID, container, false)
        binding.lifecycleOwner = this

        undoSnackbar = Snackbar.make(
            binding.snackbar,
            getString(R.string.swipe_to_dismiss),
            Snackbar.LENGTH_INDEFINITE
        ).setAction(getString(R.string.undo)) { viewModel.undoDelete() }
            .addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    viewModel.onSnackbarDismissed()
                }
            })

        setHasOptionsMenu(true)
        setUpAddItem()

        val adapter = ListDetailsAdapter(setUpTodoItemListeners())
        binding.listItems.adapter = adapter
        viewModel.todoItems.observe(viewLifecycleOwner, Observer { todoItems ->
            todoItems?.let {
                adapter.submitList(it)
            }
        })

        viewModel.showSnackbar.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {
                if (it) undoSnackbar.show()
            }
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_details_toolbar_options, menu)

        val item = menu.findItem(R.id.delete_finished)
        item.setOnMenuItemClickListener {
            viewModel.deleteFinished()
            true
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setUpAddItem() {
        with(binding) {
            addItem.setMultiLineAndDoneAction()
            addItem.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    val itemName = addItem.text.toString()
                    viewModel.createTodoItem(itemName)

                    addItem.setText("")
                }
                return@setOnEditorActionListener true
            }
        }
    }

    private fun setUpTodoItemListeners(): TodoItemListeners {
        return TodoItemListeners(
            onCheckboxSelected = { itemId: Long, isChecked: Boolean -> viewModel.onItemCompleted(itemId, isChecked) },
            onDeleted = { item -> viewModel.onDeleteItem(item) },
            onEdited = { itemId, updatedText -> viewModel.onRenameItem(itemId, updatedText)}
        )
    }

    override fun onStop() {
        super.onStop()
        viewModel.updateEditedDate()
    }


    companion object {
        const val LAYOUT_ID = R.layout.fragment_list_details
    }
}
