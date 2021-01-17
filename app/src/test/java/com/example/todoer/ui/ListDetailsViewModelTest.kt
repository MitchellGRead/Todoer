package com.example.todoer.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.testingmodule.coroutines.MainCoroutineRule
import com.example.testingmodule.mockfactories.TodoItemMockFactory
import com.example.todoer.base.ViewModelAction
import com.example.todoer.database.models.TodoItem
import com.example.todoer.domain.TodoItemRepo
import com.example.todoer.domain.TodoListRepo
import com.example.todoer.ui.createtodo.CheckList
import com.example.todoer.ui.listdetails.ListAction
import com.example.todoer.ui.listdetails.ListDetailsViewModel
import com.example.todoer.ui.listdetails.ShareAction
import com.example.todoer.ui.listdetails.SnackbarAction
import com.jraska.livedata.test
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ListDetailsViewModelTest {

    @get:Rule val mainCoroutineRule = MainCoroutineRule()
    @get:Rule val instantExecutor = InstantTaskExecutorRule()

    private val itemRepo: TodoItemRepo = mock()
    private val listRepo: TodoListRepo = mock()

    private val itemFactory = TodoItemMockFactory()
    private val list = itemFactory.todoList

    private val existingItems: MutableList<TodoItem> = mutableListOf(
        itemFactory.completedItem1,
        itemFactory.completedItem2,
        itemFactory.incompleteItem1
    )
    private lateinit var viewModel: ListDetailsViewModel

    @Before
    fun setup() {
        viewModel = ListDetailsViewModel(
            listId = list.listId,
            itemRepo = itemRepo,
            listRepo = listRepo,
            dispatcher = Dispatchers.Unconfined
        )
    }

    init {
        mainCoroutineRule.runBlockingTest {
            whenever(itemRepo.observeTodoItems(list.listId)).thenReturn(flow { emit(existingItems) })
            whenever(itemRepo.getTodoItems(list.listId)).thenReturn(existingItems)
        }
    }

    @Test
    fun `WHEN viewmodel is created THEN list items set in live data`() {
        val expected = listOf(
            itemFactory.incompleteItem1,
            itemFactory.completedItem1,
            itemFactory.completedItem2
        )

        viewModel.todoItems
            .test()
            .assertHasValue()
            .assertValue(expected)
            .assertHistorySize(1)
    }

    @Test
    fun `GIVEN item name WHEN createTodoItem THEN item inserted and list total tasks increases`() =
        mainCoroutineRule.runBlockingTest {
            val name = "whata whata"
            val createdItem = TodoItem(itemId = 1L, listId = list.listId, itemName = name)
            val beforeTotal = list.totalTasks
            existingItems.add(createdItem)
            whenever(itemRepo.getTodoItems(list.listId)).thenReturn(existingItems)

            viewModel.createTodoItem(name)

            verify(itemRepo, times(1)).insertTodoItem(list.listId, createdItem.itemName)
            verify(listRepo, times(1)).updateListTotalTasks(list.listId, beforeTotal + 1)
        }

    @Test
    fun `GIVEN no item name WHEN createTodoItem THEN item inserted with default name and list total tasks increases`() =
        mainCoroutineRule.runBlockingTest {
            val name = ""
            val createdItem = TodoItem(itemId = 1L, listId = list.listId, itemName = CheckList.getDefaultItemName())
            val beforeTotal = list.totalTasks
            existingItems.add(createdItem)
            whenever(itemRepo.getTodoItems(list.listId)).thenReturn(existingItems)

            viewModel.createTodoItem(name)

            verify(itemRepo, times(1)).insertTodoItem(list.listId, createdItem.itemName)
            verify(listRepo, times(1)).updateListTotalTasks(list.listId, beforeTotal + 1)
        }

    @Test
    fun `GIVEN incomplete item id and isChecked is true WHEN onItemCompleted THEN item updated and list completed items increased`() =
        mainCoroutineRule.runBlockingTest {
            val incompleteItem = itemFactory.incompleteItem1
            val isComplete = true
            val completedItem = incompleteItem.copy(isComplete = isComplete)
            val beforeCompleteTotal = list.completedTasks
            whenever(itemRepo.getTodoItems(list.listId)).thenReturn(existingItems.replace(incompleteItem, completedItem))

            viewModel.onItemCompleted(incompleteItem.itemId, isComplete)

            verify(itemRepo, times(1)).updateItemCompleted(incompleteItem.itemId, isComplete)
            verify(listRepo, times(1)).updateListCompleteTasks(list.listId, beforeCompleteTotal + 1)
        }

    @Test
    fun `GIVEN complete item id and isChecked is false WHEN onItemCompleted THEN item updated and list completed items decreased`() =
        mainCoroutineRule.runBlockingTest {
            val completedItem = itemFactory.completedItem1
            val isComplete = false
            val incompleteItem = completedItem.copy(isComplete = isComplete)
            val beforeCompleteTotal = list.completedTasks
            whenever(itemRepo.getTodoItems(list.listId)).thenReturn(existingItems.replace(completedItem, incompleteItem))

            viewModel.onItemCompleted(completedItem.itemId, isComplete)

            verify(itemRepo, times(1)).updateItemCompleted(completedItem.itemId, isComplete)
            verify(listRepo, times(1)).updateListCompleteTasks(list.listId, beforeCompleteTotal - 1)
        }

    @Test
    fun `GIVEN completed item WHEN onDeleteItem THEN item repo calls deleteItem and list totals adjusted`() =
        mainCoroutineRule.runBlockingTest {
            val item = itemFactory.completedItem1
            val beforeCompleteTotal = list.completedTasks
            val beforeTotal = list.totalTasks
            existingItems.remove(item)
            whenever(itemRepo.getTodoItems(list.listId)).thenReturn(existingItems)

            viewModel.onDeleteItem(item)

            verify(itemRepo, times(1)).deleteItems(listOf(item))
            verify(listRepo, times(1)).updateListCompleteTasks(list.listId, beforeCompleteTotal - 1)
            verify(listRepo, times(1)).updateListTotalTasks(list.listId, beforeTotal - 1)
            viewModel.action
                .test()
                .assertHasValue()
                .assertHistorySize(1)
            val actual = (viewModel.action.value?.getContentIfNotHandled() as SnackbarAction).shouldShow
            assertEquals(true, actual)
        }

    @Test
    fun `GIVEN delete all completed WHEN deleteFinished THEN items are deleted`() =
        mainCoroutineRule.runBlockingTest {
            val item = itemFactory.completedItem1
            val item2 = itemFactory.completedItem2

            viewModel.deleteFinished()

            verify(itemRepo, times(1)).deleteItems(listOf(item, item2))
            viewModel.action
                .test()
                .assertHasValue()
                .assertHistorySize(1)
            val actual = (viewModel.action.value?.getContentIfNotHandled() as SnackbarAction).shouldShow
            assertEquals(true, actual)
        }

    @Test
    fun `GIVEN items deleted WHEN undoDelete THEN items restored`() =
        mainCoroutineRule.runBlockingTest {
            val item = itemFactory.completedItem1
            val item2 = itemFactory.completedItem2
            viewModel.deleteFinished()

            viewModel.undoDelete()

            verify(itemRepo, times(1)).insertExisitingTodoItems(listOf(item, item2))
        }

    @Test
    fun `GIVEN updated name for item WHEN onRenameName THEN verify item renamed`() =
        mainCoroutineRule.runBlockingTest {
            val item = itemFactory.completedItem1
            val newName = "new name"

            viewModel.onRenameItem(item.itemId, newName)

            verify(itemRepo, times(1)).updateItemName(item.itemId, newName)
        }

    @Test
    fun `WHEN shareTodo THEN all incomplete items formatted to string for sharing`() =
        mainCoroutineRule.runBlockingTest {
            val item = itemFactory.incompleteItem1
            val shareData = "- ${item.itemName}"

            viewModel.shareTodo()

            viewModel.action
                .test()
                .assertHasValue()
                .assertHistorySize(1)
            val actual = (viewModel.action.value?.getContentIfNotHandled() as ShareAction).data
            assertEquals(shareData, actual)
        }


    /* Helper Functions */
    private fun List<TodoItem>.replace(current: TodoItem, new: TodoItem): List<TodoItem> {
        return this.map { item ->
            if (item == current) new else item
        }
    }
}
