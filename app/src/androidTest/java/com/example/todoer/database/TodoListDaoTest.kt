package com.example.todoer.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.testingmodule.coroutines.MainCoroutineRule
import com.example.testingmodule.mockfactories.TodoListMockFactory
import com.example.todoer.database.models.TodoList
import junit.framework.TestCase.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.joda.time.DateTime
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class TodoListDaoTest {

    @get:Rule val mainCoroutineRule = MainCoroutineRule()
    @get:Rule val instantExecutor = InstantTaskExecutorRule()

    private val listFactory = TodoListMockFactory()

    private lateinit var database: TodoDatabase
    private lateinit var todoListDao: TodoListDao

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().context
        database = Room.inMemoryDatabaseBuilder(context, TodoDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        todoListDao = database.todoListDao()
    }

    @After
    fun cleanUp() {
        database.close()
    }

    /* Inserting Tests */
    @Test
    fun `GIVEN_valid_todo_list_WHEN_insertTodoList_THEN_list_is_inserted`() =
        mainCoroutineRule.runBlockingTest {
            val expected = TodoList(404L)
            assertNull(getList(expected.listId))

            val listId = todoListDao.insertTodoList(expected)

            val actual = getList(listId)
            assertEquals(expected, actual)
        }

    /* Updating Tests */
    @Test
    fun `GIVEN_new_total_tasks_count_WHEN_updateListTotalTasks_THEN_total_tasks_are_updated`() =
        mainCoroutineRule.runBlockingTest {
            val list = listFactory.todoList1
            val listId = todoListDao.insertTodoList(list)
            val expectedTotal = 404
            assertEquals(list.totalTasks, getList(listId)?.totalTasks)
            assertNotSame(expectedTotal, list.totalTasks)

            todoListDao.updateListTotalTasks(listId, expectedTotal)

            val actual = getList(listId)
            assertNotSame(list.totalTasks, actual?.totalTasks)
            assertEquals(expectedTotal, actual?.totalTasks)
        }

    @Test
    fun `GIVEN_new_completed_tasks_count_WHEN_updateListCompletedTasks_THEN_completed_tasks_are_updated`() =
        mainCoroutineRule.runBlockingTest {
            val list = listFactory.todoList1
            val listId = todoListDao.insertTodoList(list)
            val expectedComplete = 0
            assertEquals(list.completedTasks, getList(listId)?.completedTasks)
            assertNotSame(list.completedTasks, expectedComplete)

            todoListDao.updateListCompletedTasks(listId, expectedComplete)

            val actual = getList(listId)
            assertNotSame(list.completedTasks, actual?.completedTasks)
            assertEquals(expectedComplete, actual?.completedTasks)
        }

    @Test
    fun `GIVEN_edit_date_WHEN_updateEditDate_THEN_date_is_updated`() =
        mainCoroutineRule.runBlockingTest {
            val list = listFactory.todoList1
            val listId = todoListDao.insertTodoList(list)
            val expectedDate = DateTime()
            assertEquals(list.editedAt, getList(listId)?.editedAt)
            assertNotSame(expectedDate, list.editedAt)

            todoListDao.updateEditDate(listId, expectedDate)

            val actual = getList(listId)
            assertNotSame(list.editedAt, actual?.editedAt)
            assertEquals(expectedDate, actual?.editedAt)
        }

    @Test
    fun `GIVEN_list_favourited_WHEN_updateIsFavourited_THEN_favourited_toggles`() =
        mainCoroutineRule.runBlockingTest {
            val list = listFactory.todoList1
            val listId = todoListDao.insertTodoList(list)
            val expectedBoolean = list.isFavourited.not()
            assertEquals(list.isFavourited, getList(listId)?.isFavourited)
            assertNotSame(expectedBoolean, list.isFavourited)

            todoListDao.updatedIsFavourited(listId, expectedBoolean)

            val actual = getList(listId)
            assertNotSame(list.isFavourited, actual?.isFavourited)
            assertEquals(expectedBoolean, actual?.isFavourited)
        }

    /* Getting Tests */
    @Test
    fun `WHEN_observeTodoLists_THEN_flow_with_current_lists_received`() =
        mainCoroutineRule.runBlockingTest {
            todoListDao.insertTodoList(listFactory.todoList1)
            todoListDao.insertTodoList(listFactory.todoList2)
            val expected: List<TodoList> = listFactory.todoLists

            val actual = todoListDao.observeTodoLists().take(1).toList()[0]

            assertEquals(expected, actual)
        }

    /* Deleting Tests */
    @Test
    fun `GIVEN_existing_id_WHEN_deleteListById_THEN_list_removed_from_database`() =
        mainCoroutineRule.runBlockingTest {
            val listId = todoListDao.insertTodoList(listFactory.todoList1)
            assertNotNull(getList(listId))

            todoListDao.deleteListById(listId)

            assertNull(getList(listId))
        }

    /* Helpers */
    private suspend fun getList(id: Long): TodoList? {
        return todoListDao.getTodoList(id)
    }
}
