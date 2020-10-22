package com.example.todoer.ui.homescreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.todoer.R
import com.example.todoer.databinding.FragmentHomeScreenBinding
import com.example.todoer.ui.homescreen.recycler.TodoListAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class HomeScreenFragment : Fragment() {

    @Inject lateinit var homeScreenRepo: HomeScreenRepo

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("Creating Home Screen fragment view")

        val binding: FragmentHomeScreenBinding = DataBindingUtil.inflate(inflater, LAYOUT_ID, container, false)
        val viewModelFactory = HomeScreenViewModelFactory(homeScreenRepo)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(HomeScreenViewModel::class.java)
        val adapter = TodoListAdapter()

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.todoList.adapter = adapter

        adapter.submitList(viewModel._todoLists)
        return binding.root
    }

    companion object {
        val LAYOUT_ID = R.layout.fragment_home_screen
    }
}
