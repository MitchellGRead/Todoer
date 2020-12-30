package com.example.todoer.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.testingmodule.coroutines.MainCoroutineRule
import com.example.testingmodule.mockfactories.TodoItemMockFactory
import com.example.todoer.database.models.TodoItem
import junit.framework.TestCase.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class TodoItemDaoTest {

    @get:Rule val mainCoroutineRule = MainCoroutineRule()
    @get:Rule val instantExecutor = InstantTaskExecutorRule()

    private val itemFactory = TodoItemMockFactory()
    private val todoList = itemFactory.todoList
    private val rogueList = itemFactory.rogueList

    private lateinit var database: TodoDatabase
    private lateinit var todoItemDao: TodoItemDao
    private lateinit var todoListDao: TodoListDao

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().context
        database = Room.inMemoryDatabaseBuilder(context, TodoDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        todoListDao = database.todoListDao()
        todoItemDao = database.todoItemDao()

        mainCoroutineRule.runBlockingTest {
            todoListDao.insertTodoList(todoList)
            todoListDao.insertTodoList(rogueList)
        }
    }

    @After
    fun cleanUp() {
        database.close()
    }

    /* Inserting Tests */
    @Test
    fun `GIVEN_valid_todo_item_WHEN_insertTodoItem_THEN_item_is_inserted`() =
        mainCoroutineRule.runBlockingTest {
            val expected = itemFactory.completedItem1
            assertNull(getItem(expected.itemId))

            val itemId = todoItemDao.insertTodoItem(expected)

            val actual = getItem(itemId)
            assertEquals(expected, actual)
        }

    /* Updating Tests */
    @Test
    fun `GIVEN_item_is_completed_WHEN_updateItemCompleted_THEN_item_marked_as_complete_updated`() =
        mainCoroutineRule.runBlockingTest {
            val item = itemFactory.incompleteItem1
            val itemId = todoItemDao.insertTodoItem(item)
            val expectedCompleted = true
            assertEquals(item.isComplete, getItem(itemId)?.isComplete)
            assertNotSame(expectedCompleted, item.isComplete)

            todoItemDao.updateItemCompleted(itemId, expectedCompleted)

            val actual = getItem(itemId)
            assertNotSame(item.isComplete, actual?.isComplete)
            assertEquals(expectedCompleted, actual?.isComplete)
        }

    @Test
    fun `GIVEN_new_item_name_WHEN_updateItemName_THEN_name_is_updated`() =
        mainCoroutineRule.runBlockingTest {
            val item = itemFactory.incompleteItem1
            val itemId = todoItemDao.insertTodoItem(item)
            val expectedName = "what a new name"
            assertEquals(item.itemName, getItem(itemId)?.itemName)
            assertNotSame(expectedName, item.itemName)

            todoItemDao.updateItemName(itemId, expectedName)

            val actual = getItem(itemId)
            assertNotSame(item.itemName, actual?.itemName)
            assertEquals(expectedName, actual?.itemName)
        }

    /* Getting Tests */
    @Test
    fun `GIVEN_valid_list_id_WHEN_getTodoItemsInList_THEN_return_all_items_in_list`() =
        mainCoroutineRule.runBlockingTest {
            val listId = todoList.listId
            todoItemDao.insertTodoItem(itemFactory.incompleteItem1)
            todoItemDao.insertTodoItem(itemFactory.completedItem1)
            todoItemDao.insertTodoItem(itemFactory.rogueItem)
            val expected: List<TodoItem> = itemFactory.listTodoItems

            val actual = todoItemDao.getTodoItemsInList(listId)

            assertNotNull(actual)
            assert(itemFactory.rogueItem !in expected)
            assertEquals(expected, actual)
        }

    @Test
    fun `GIVEN_valid_list_id_WHEN_observeTodoItems_THEN_flow_of_items_in_default_order`() =
        mainCoroutineRule.runBlockingTest {
            val listId = todoList.listId
            todoItemDao.insertTodoItem(itemFactory.incompleteItem1)
            todoItemDao.insertTodoItem(itemFactory.completedItem1)
            todoItemDao.insertTodoItem(itemFactory.rogueItem)
            val expected: List<TodoItem> = itemFactory.listTodoItems

            val actual = todoItemDao.observeTodoItemsInList(listId).take(1).toList()[0]

            assert(itemFactory.rogueItem !in expected)
            assertEquals(expected, actual)
        }

    /* Deleting Tests */
    @Test
    fun `GIVEN_existing_id_WHEN_deleteListById_THEN_list_removed_from_database`() =
        mainCoroutineRule.runBlockingTest {
            val itemId = todoItemDao.insertTodoItem(itemFactory.completedItem1)
            assertNotNull(getItem(itemId))

            todoItemDao.deleteItemById(itemId)

            assertNull(getItem(itemId))
        }

    @Test
    fun `WHEN_list_gets_deleted_THEN_items_removed_from_database`() =
        mainCoroutineRule.runBlockingTest {
            val listId = todoList.listId
            val itemId = todoItemDao.insertTodoItem(itemFactory.completedItem2)
            assertNotNull(getItem(itemId))

            todoListDao.deleteListById(listId)

            assertNull(getItem(itemId))
        }

    /* Helpers */
    private suspend fun getItem(id: Long): TodoItem? {
        return todoItemDao.getTodoItem(id)
    }
}
