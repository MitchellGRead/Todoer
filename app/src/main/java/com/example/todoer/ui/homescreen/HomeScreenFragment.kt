package com.example.todoer.ui.homescreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todoer.R
import com.example.todoer.databinding.FragmentHomeScreenBinding
import com.example.todoer.ui.homescreen.recycler.TodoListAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeScreenFragment : Fragment() {

    private lateinit var viewModel: HomeScreenViewModel
    private lateinit var binding: FragmentHomeScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("Creating Home Screen fragment view")

        binding = DataBindingUtil.inflate(inflater, LAYOUT_ID, container, false)
        viewModel = ViewModelProvider(this).get(HomeScreenViewModel::class.java)
        val adapter = TodoListAdapter()

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.todoList.adapter = adapter

        setUpFabClickHandler()
        setUpCreateListNavigation()

        viewModel.todoLists.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
        return binding.root
    }

    private fun setUpFabClickHandler() {
        binding.addListFab.setOnClickListener {
            viewModel.onFabButtonClicked()
        }
    }

    private fun setUpCreateListNavigation() {
        viewModel.navigateToCreateList.observe(viewLifecycleOwner, Observer {
            if (it) {
                this.findNavController().navigate(HomeScreenFragmentDirections.actionHomeScreenFragmentToCreateListFragment())
                viewModel.onCreateListNavigated()
            }
        })
    }

    companion object {
        val LAYOUT_ID = R.layout.fragment_home_screen
    }
}
