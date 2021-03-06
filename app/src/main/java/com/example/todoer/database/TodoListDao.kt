package com.example.todoer.database

import androidx.room.*
import com.example.todoer.database.models.TodoList
import kotlinx.coroutines.flow.Flow
import org.joda.time.DateTime

@Dao
interface TodoListDao {

    /* Inserting List Queries */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodoList(todoList: TodoList): Long

    /* Updating List Queries */
    @Query(value = "UPDATE todo_list_table SET total_tasks = :totalTasks_ WHERE list_id = :listId_")
    suspend fun updateListTotalTasks(listId_: Long, totalTasks_: Int)

    @Query(value = "UPDATE todo_list_table SET completed_tasks = :completedTasks_ WHERE list_id = :listId_")
    suspend fun updateListCompletedTasks(listId_: Long, completedTasks_: Int)

    @Query(value = "UPDATE todo_list_table SET list_name = :updatedName_ WHERE list_id = :listId_")
    suspend fun updateListName(listId_: Long, updatedName_: String)

    @Query(value = "UPDATE todo_list_table SET edited_at = :editedDate_ WHERE list_id = :listId_")
    suspend fun updateEditDate(listId_: Long, editedDate_: DateTime)

    @Query(value = "UPDATE todo_list_table SET favourited = :isFavourited_ WHERE list_id = :listId_")
    suspend fun updatedIsFavourited(listId_: Long, isFavourited_: Boolean)

    /* Getting List Queries */
    @Query(value = "SELECT * FROM todo_list_table")
    fun observeTodoLists(): Flow<List<TodoList>>

    @Query(value = "SELECT * FROM todo_list_table WHERE list_id = :listId_")
    suspend fun getTodoList(listId_: Long): TodoList?

    /* Deleting List Queries */
    @Query(value = "DELETE FROM todo_list_table WHERE list_id = :listId_")
    suspend fun deleteListById(listId_: Long)
}
