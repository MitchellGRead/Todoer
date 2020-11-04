package com.example.todoer.ui.createlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todoer.R
import com.example.todoer.databinding.FragmentCreateListBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class CreateListFragment : Fragment() {

    private val viewModel: CreateListViewModel by viewModels()
    private lateinit var binding: FragmentCreateListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("Creating CreateList fragment view")

        binding = DataBindingUtil.inflate(inflater, LAYOUT_ID, container, false)
        binding.lifecycleOwner = this

        setUpNavigation()
        setUpCreateListButton()

        return binding.root
    }

    private fun setUpNavigation() {
        viewModel.navigateToHomeScreen.observe(viewLifecycleOwner, Observer {
            if (it) {
                this.findNavController().navigate(CreateListFragmentDirections.actionCreateListFragmentToHomeScreenFragment())
                viewModel.onHomeScreenNavigated()
            }
        })
    }

    private fun setUpCreateListButton() {
        binding.createList.setOnClickListener {
            val listName = binding.editListName.text.toString()
            viewModel.onCreateList(listName)
        }
    }

    companion object {
        const val LAYOUT_ID = R.layout.fragment_create_list
    }
}
