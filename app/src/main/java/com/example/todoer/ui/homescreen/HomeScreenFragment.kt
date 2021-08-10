package com.example.todoer.ui.homescreen

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.todoer.R
import com.example.todoer.base.BaseFragment
import com.example.todoer.base.ViewModelAction
import com.example.todoer.databinding.FragmentHomeScreenBinding
import com.example.todoer.ui.homescreen.recycler.HomeScreenAdapter
import com.example.todoer.ui.homescreen.recycler.HomeScreenItem
import com.example.todoer.ui.homescreen.recycler.TodoCardListeners
import com.example.todoer.ui.listdetails.ListDetailsViewModel
import com.example.todoer.utils.SharedPreferencesUtils.getBooleanValue
import com.example.todoer.utils.SharedPreferencesUtils.setBooleanValue
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeScreenFragment : BaseFragment() {

    private lateinit var binding: FragmentHomeScreenBinding
    private lateinit var undoSnackbar: Snackbar
    private val viewModel: HomeScreenViewModel by viewModels()
    private var sharedPrefs: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("Creating Home Screen fragment view")

        sharedPrefs = activity?.getPreferences(Context.MODE_PRIVATE)
        binding = DataBindingUtil.inflate(inflater, LAYOUT_ID, container, false)
        binding.lifecycleOwner = this

        makeSnackbar()
        setHasOptionsMenu(true)
        setupFabClickHandler()
        setupNavigation()

        val adapter = HomeScreenAdapter(setupTodoListListeners())
        binding.todoList.adapter = adapter
        viewModel.homeScreenItems.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.action.observe(viewLifecycleOwner, Observer { action ->
            action?.let { onAction(it) }
        })

        return binding.root
    }

    /* Options Menu */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_screen_toolbar_options, menu)

        // Setting up favourites sorting
        val favouriteToTop = menu.findItem(R.id.favourites_to_top)
        val optionChecked = getFavouritesToTopValue()
        favouriteToTop.isChecked = optionChecked
        favouriteToTop.setOnMenuItemClickListener { onFavouriteOption(it) }
        viewModel.sortContent(optionChecked)

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun getFavouritesToTopValue(): Boolean {
        return sharedPrefs.getBooleanValue(
            getString(FAVOURITES_TO_TOP_PREF_KEY),
            false
        )
    }

    private fun onFavouriteOption(item: MenuItem): Boolean {
        val optionCheckedSwapped = getFavouritesToTopValue().not()
        sharedPrefs.setBooleanValue(
            getString(FAVOURITES_TO_TOP_PREF_KEY),
            optionCheckedSwapped
        )
        item.isChecked = optionCheckedSwapped
        viewModel.sortContent(optionCheckedSwapped)
        return true
    }

    /* FAB Button */
    private fun setupFabClickHandler() {
        binding.addTodoFab.setOnClickListener {
            viewModel.onFabButtonClicked()
        }
    }

    /* Navigation */
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

    /* Card Actions */
    private fun setupTodoListListeners() =
        TodoCardListeners (
            onClickTodoCard = { homeScreenItem -> viewModel.onHomeScreenItemClicked(homeScreenItem) },
            renameTodoListener = { homeScreenItem, updatedName -> viewModel.onRenameTodo(homeScreenItem, updatedName) },
            deleteTodoListener = { homeScreenItem -> viewModel.onDeleteTodo(homeScreenItem) },
            onCardFavouritedListener = { homeScreenItem, isFavourited -> viewModel.onTodoFavourited(homeScreenItem, isFavourited) }
        )

    /* Fragment Actions */
    private fun makeSnackbar() {
        undoSnackbar = Snackbar.make(
            binding.snackbar,
            getString(R.string.swipe_to_dismiss),
            Snackbar.LENGTH_INDEFINITE
        )        .setAction(getString(R.string.undo)) { viewModel.undoDelete() }
            .addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    viewModel.onSnackbarDismissed()
                }
            })
    }

    private fun onAction(action: ViewModelAction<HomeAction>) {
        when (val homeAction = action.getContentIfNotHandled()) {
            is SnackbarAction -> if (homeAction.shouldShow) undoSnackbar.show() else makeSnackbar()
            is ShareAction -> TODO()
            null -> Timber.e("Error: $action has null homeAction type.")
        }
    }

    companion object {
        const val LAYOUT_ID = R.layout.fragment_home_screen
        const val FAVOURITES_TO_TOP_PREF_KEY = R.string.home_screen_favourites_to_top_option
    }
}
