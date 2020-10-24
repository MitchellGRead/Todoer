package com.example.todoer.ui.createlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.todoer.R
import com.example.todoer.databinding.FragmentCreateListBinding
import com.example.todoer.databinding.FragmentHomeScreenBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class CreateListFragment : Fragment() {

    private lateinit var viewModel: CreateListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("Creating Create List fragment view")

        val binding: FragmentCreateListBinding = DataBindingUtil.inflate(inflater, LAYOUT_ID, container, false)
        viewModel = ViewModelProvider(this).get(CreateListViewModel::class.java)

        binding.lifecycleOwner = this

        return binding.root
    }

    companion object {
        val LAYOUT_ID = R.layout.fragment_create_list
    }
}
