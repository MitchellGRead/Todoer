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
import com.example.todoer.base.BaseFragment
import com.example.todoer.databinding.FragmentHomeScreenBinding
import com.example.todoer.ui.homescreen.recycler.HomeScreenAdapter
import com.example.todoer.ui.homescreen.recycler.TodoCardListeners
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeScreenFragment : BaseFragment() {

    private lateinit var binding: FragmentHomeScreenBinding
    private val viewModel: HomeScreenViewModel by viewModels()

    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("Creating Home Screen fragment view")

        binding = DataBindingUtil.inflate(inflater, LAYOUT_ID, container, false)
        binding.lifecycleOwner = this

        setupFabClickHandler()
        setupNavigation()

        val adapter = HomeScreenAdapter(setupTodoListListeners())
        binding.todoList.adapter = adapter
        viewModel.homeScreenItems.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }

    private fun setupFabClickHandler() {
        binding.addTodoFab.setOnClickListener {
            viewModel.onFabButtonClicked()
        }
    }

    private fun setupNavigation() {
        // Create navigation
        viewModel.navigateToCreateTodo.observe(viewLifecycleOwner, Observer {
            if (it) {
                this.findNavController().navigate(HomeScreenFragmentDirections.actionHomeScreenFragmentToCreateTodoFragment())
                viewModel.onCreateListNavigated()
            }
        })

        // Checklist details navigation
        viewModel.navigateToListDetails.observe(viewLifecycleOwner, Observer { listDetailArgs ->
            listDetailArgs?.let {
                this.findNavController().navigate(HomeScreenFragmentDirections.actionHomeScreenFragmentToListDetailsFragment(it))
                viewModel.onTodoListNavigated()
            }
        })

        // Note details navigation
        viewModel.navigateToNoteDetails.observe(viewLifecycleOwner, Observer { noteDetailArgs ->
            noteDetailArgs?.let {
                this.findNavController().navigate(HomeScreenFragmentDirections.actionHomeScreenFragmentToNoteDetailsFragment(it))
                viewModel.onTodoNoteNavigated()
            }
        })
    }

    private fun setupTodoListListeners() =
        TodoCardListeners (
            onClickTodoCard = { homeScreenItem -> viewModel.onHomeScreenItemClicked(homeScreenItem) },
            renameTodoListener = { homeScreenItem, updatedName -> viewModel.onRenameTodo(homeScreenItem, updatedName) },
            deleteTodoListener = { homeScreenItem -> viewModel.onDeleteTodo(homeScreenItem) },
            onCardFavouritedListener = { homeScreenItem, isFavourited -> viewModel.onTodoFavourited(homeScreenItem, isFavourited) }
        )


    companion object {
        const val LAYOUT_ID = R.layout.fragment_home_screen
    }
}
