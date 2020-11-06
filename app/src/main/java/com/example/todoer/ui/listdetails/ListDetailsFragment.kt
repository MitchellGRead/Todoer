package com.example.todoer.ui.listdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.todoer.R
import com.example.todoer.databinding.FragmentListDetailsBinding
import com.example.todoer.ui.listdetails.recycler.ListDetailsAdapter
import com.example.todoer.ui.listdetails.recycler.TodoItemListeners
import com.example.todoer.utils.ViewUtils.setMultiLineAndDoneAction
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ListDetailsFragment : Fragment() {

    private lateinit var binding: FragmentListDetailsBinding
    private val args: ListDetailsFragmentArgs by navArgs()

    @Inject lateinit var viewModelAssistedInjectFactory: ListDetailsViewModel.AssistedFactory
    private val viewModel: ListDetailsViewModel by viewModels {
        ListDetailsViewModel.provideFactory(viewModelAssistedInjectFactory, args.todoListId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.d("Creating ListDetails fragment")

        binding = DataBindingUtil.inflate(inflater, LAYOUT_ID, container, false)
        binding.lifecycleOwner = this

        setUpAddItem()

        val adapter = ListDetailsAdapter(setUpTodoItemListeners())
        binding.listItems.adapter = adapter
        viewModel.todoItems.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }

    private fun setUpAddItem() {
        with(binding) {
            addItem.setMultiLineAndDoneAction()
            addItem.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    val itemName = addItem.text.toString()
                    viewModel.insertTodoItem(itemName)

                    addItem.setText("")
                }
                return@setOnEditorActionListener true
            }
        }
    }

    private fun setUpTodoItemListeners(): TodoItemListeners {
        return TodoItemListeners(
            onCheckboxSelected = { itemId: Long, isChecked: Boolean -> viewModel.onItemCompleted(itemId, isChecked) },
            onDeleted = { itemId -> viewModel.onDeleteItem(itemId) }
        )
    }

    companion object {
        const val LAYOUT_ID = R.layout.fragment_list_details
    }
}
