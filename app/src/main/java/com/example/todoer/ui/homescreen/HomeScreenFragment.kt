package com.example.todoer.ui.homescreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.todoer.R
import com.example.todoer.databinding.FragmentHomeScreenBinding
import com.example.todoer.ui.homescreen.recycler.HomeScreenAdapter
import com.example.todoer.ui.homescreen.recycler.TodoListListeners
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeScreenFragment : Fragment() {

    private lateinit var binding: FragmentHomeScreenBinding
    private val viewModel: HomeScreenViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("Creating Home Screen fragment view")

        binding = DataBindingUtil.inflate(inflater, LAYOUT_ID, container, false)
        binding.lifecycleOwner = this

        setupFabClickHandler()
        setupCreateListNavigation()
        setUpTodoListNavigation()

        val adapter = HomeScreenAdapter(setupTodoListListeners(), activity)
        binding.todoList.adapter = adapter
        viewModel.todoLists.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }

    private fun setupFabClickHandler() {
        binding.addListFab.setOnClickListener {
            viewModel.onFabButtonClicked()
        }
    }

    private fun setupCreateListNavigation() {
        viewModel.navigateToCreateList.observe(viewLifecycleOwner, Observer {
            if (it) {
                this.findNavController().navigate(HomeScreenFragmentDirections.actionHomeScreenFragmentToCreateListFragment())
                viewModel.onCreateListNavigated()
            }
        })
    }

    private fun setUpTodoListNavigation() {
        viewModel.navigateToTodoListNav.observe(viewLifecycleOwner, Observer { listDetailArgs ->
            listDetailArgs?.let {
                this.findNavController().navigate(HomeScreenFragmentDirections.actionHomeScreenFragmentToListDetailsFragment(it))
                viewModel.onTodoListNavigated()
            }
        })
    }

    private fun setupTodoListListeners() =
        TodoListListeners (
            onClickList = { listId, listName -> viewModel.onTodoListClicked(listId, listName) },
            renameClickListener = { listId, updatedName -> viewModel.onRenameList(listId, updatedName) },
            deleteClickListener = { listId -> viewModel.onDeleteList(listId) },
            shareClickListener = { listId -> viewModel.onShareList(listId) }
        )


    companion object {
        const val LAYOUT_ID = R.layout.fragment_home_screen
    }
}
