package com.example.todoer.ui.createtodo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.todoer.R
import com.example.todoer.base.BaseFragment
import com.example.todoer.databinding.FragmentCreateTodoBinding
import com.example.todoer.utils.ContextUtils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class CreateTodoFragment : BaseFragment() {

    private val viewModel: CreateTodoViewModel by viewModels()
    private lateinit var binding: FragmentCreateTodoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("Creating CreateList fragment view")

        binding = DataBindingUtil.inflate(inflater, LAYOUT_ID, container, false)
        binding.lifecycleOwner = this

        setUpNavigation()
        setUpTodoTypeSpinner()
        setUpCreateTodoButton()

        return binding.root
    }

    private fun setUpNavigation() {
        viewModel.navigateToTodoList.observe(viewLifecycleOwner, Observer { listDetailArgs ->
            listDetailArgs?.let {
                this.findNavController().navigate(CreateTodoFragmentDirections.actionCreateTodoFragmentToListDetailsFragment(it))
                viewModel.onTodoListNavigated()
            }
        })

        viewModel.navigateToTodoNote.observe(viewLifecycleOwner, Observer { noteDetailArgs ->
            noteDetailArgs?.let {
                this.findNavController().navigate(CreateTodoFragmentDirections.actionCreateTodoFragmentToNoteDetailsFragment(it))
                viewModel.onTodoNoteNavigated()
            }
        })
    }

    private fun setUpTodoTypeSpinner() {
        val spinner: Spinner = binding.todoType
        context?.let { context ->
            ArrayAdapter.createFromResource(
                context,
                R.array.create_todo_type_options,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }
        }
    }

    private fun setUpCreateTodoButton() {
        with(binding) {
            createTodo.setOnClickListener {
                val todoName = newTodoName.text.toString()
                val todoType = todoType.selectedItem.toString()

                viewModel.onCreateTodo(todoName, todoType)
                context?.hideKeyboard(view = newTodoName)
            }
        }
    }

    companion object {
        const val LAYOUT_ID = R.layout.fragment_create_todo
    }
}
