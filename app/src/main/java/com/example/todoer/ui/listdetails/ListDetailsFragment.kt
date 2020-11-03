package com.example.todoer.ui.listdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.todoer.R
import com.example.todoer.databinding.FragmentListDetailsBinding
import com.example.todoer.ui.listdetails.recycler.ListDetailsAdapter
import com.example.todoer.utils.ViewUtils.setMultiLineAndDoneAction
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ListDetailsFragment : Fragment() {

    private lateinit var viewModel: ListDetailsViewModel
    private lateinit var binding: FragmentListDetailsBinding

    private val args: ListDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.d("Creating ListDetails fragment")

        binding = DataBindingUtil.inflate(inflater, LAYOUT_ID, container, false)
        viewModel = ViewModelProvider(this).get(ListDetailsViewModel::class.java)
        val adapter = ListDetailsAdapter()

        binding.lifecycleOwner = this
        binding.listItems.adapter = adapter

        val listId = args.todoListId
        setUpAddItem(listId)

        viewModel.todoItems.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }

    private fun setUpAddItem(listId: Long) {
        with(binding) {
            addItem.setMultiLineAndDoneAction()
            addItem.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    val itemName = addItem.text.toString()
                    viewModel.insertTodoItem(listId, itemName)

                    addItem.setText("")
                }
                return@setOnEditorActionListener true
            }
        }
    }

    companion object {
        const val LAYOUT_ID = R.layout.fragment_list_details
    }
}
