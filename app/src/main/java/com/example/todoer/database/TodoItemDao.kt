package com.example.todoer.database

import androidx.room.*
import com.example.todoer.database.models.TodoItem
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoItemDao {

    /* Inserting Item Queries */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodoItem(item: TodoItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodoItems(items: List<TodoItem>)

    /* Updating Item Queries */
    @Query(value = "UPDATE todo_item_table SET is_complete = :isCompleted_ WHERE item_id = :itemId_")
    suspend fun updateItemCompleted(itemId_: Long, isCompleted_: Boolean)

    @Query(value = "UPDATE todo_item_table SET item_name = :updatedName_ WHERE item_id = :itemId_")
    suspend fun updateItemName(itemId_: Long, updatedName_: String)

    /* Retrieving Item Queries */
    @Query(value = "SELECT * FROM todo_item_table WHERE item_Id = :itemId_")
    suspend fun getTodoItem(itemId_: Long): TodoItem?

    @Query(value = "SELECT * FROM todo_item_table WHERE list_id = :listId_")
    suspend fun getTodoItemsInList(listId_: Long): List<TodoItem>?

    @Query(value = "SELECT * FROM todo_item_table WHERE list_id = :listId_ ORDER BY created_at")
    fun observeTodoItemsInList(listId_: Long): Flow<List<TodoItem>>

    /* Deleting Item Queries */
    @Delete
    suspend fun deleteItem(item: TodoItem)

    @Delete
    suspend fun deleteItems(items: List<TodoItem>)
}
