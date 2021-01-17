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
import com.example.todoer.base.ViewModelAction
import com.example.todoer.base.BaseFragment
import com.example.todoer.databinding.FragmentListDetailsBinding
import com.example.todoer.sharing.ShareIntentFactory
import com.example.todoer.ui.listdetails.recycler.ListDetailsAdapter
import com.example.todoer.ui.listdetails.recycler.TodoItemListeners
import com.example.todoer.utils.ViewUtils.setMultiLineAndDoneAction
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ListDetailsFragment : BaseFragment() {

    private lateinit var binding: FragmentListDetailsBinding
    private lateinit var undoSnackbar: Snackbar
    private val args: ListDetailsFragmentArgs by navArgs()

    @Inject lateinit var shareIntentFactory: ShareIntentFactory
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

        makeSnackbar()
        setHasOptionsMenu(true)
        setUpAddItem()

        val adapter = ListDetailsAdapter(setUpTodoItemListeners())
        binding.listItems.adapter = adapter
        viewModel.todoItems.observe(viewLifecycleOwner, Observer { todoItems ->
            todoItems?.let {
                adapter.submitList(it)
            }
        })

        viewModel.action.observe(viewLifecycleOwner, Observer { action ->
            action?.let { onAction(it) }
        })

        return binding.root
    }

    private fun onAction(action: ViewModelAction<ListAction>) {
        val listAction = action.getContentIfNotHandled()
        when (listAction) {
            is SnackbarAction -> {
                if (listAction.shouldShow) undoSnackbar.show() else makeSnackbar()
            }
            is ShareAction -> {
                val shareIntent = shareIntentFactory.createTextShareIntent(listAction.data)
                startActivity(shareIntent)
            }
            null -> Timber.e("Error: $action has a null listAction type.")
        }
    }

    private fun makeSnackbar() {
        undoSnackbar = Snackbar.make(
            binding.snackbar,
            getString(R.string.swipe_to_dismiss),
            Snackbar.LENGTH_INDEFINITE
        )        .setAction(getString(R.string.undo)) { viewModel.undoDelete() }
            .addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    viewModel.onSnackbarDismissed()
                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_details_toolbar_options, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_finished -> {
                viewModel.deleteFinished()
                true
            }
            R.id.share_todo -> {
                viewModel.shareTodo()
                true
            }
            else -> false
        }
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
