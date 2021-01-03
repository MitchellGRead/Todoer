package com.example.todoer.ui.homescreen.recycler

data class TodoCardListeners(
    val onClickTodoCard: (homeScreenItem: HomeScreenItem) -> Unit,
    val renameTodoListener: (homeScreenItem: HomeScreenItem, updatedName: String) -> Unit,
    val deleteTodoListener: (homeScreenItem: HomeScreenItem) -> Unit,
    val onCardFavouritedListener: (homeScreenItem: HomeScreenItem, isFavourited: Boolean) -> Unit
)
