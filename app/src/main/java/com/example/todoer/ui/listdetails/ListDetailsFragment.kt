package com.example.todoer.ui.listdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.todoer.R
import com.example.todoer.databinding.FragmentListDetailsBinding
import com.example.todoer.ui.listdetails.recycler.ListDetailsAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ListDetailsFragment : Fragment() {

    private lateinit var viewModel: ListDetailsViewModel
    private lateinit var binding: FragmentListDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("Creating ListDetails fragment")

        binding = DataBindingUtil.inflate(inflater, LAYOUT_ID, container, false)
        viewModel = ViewModelProvider(this).get(ListDetailsViewModel::class.java)
        val adapter = ListDetailsAdapter()

        binding.lifecycleOwner = this
        binding.listItems.adapter = adapter

        viewModel.todoItems.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }

    companion object {
        const val LAYOUT_ID = R.layout.fragment_list_details
    }
}
