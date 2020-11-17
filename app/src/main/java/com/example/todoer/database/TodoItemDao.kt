package com.example.todoer.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todoer.database.models.TodoItem

@Dao
interface TodoItemDao {

    /* Inserting Item Queries */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodoItem(item: TodoItem)

    /* Updating Item Queries */
    @Update
    suspend fun updateItem(todoItem: TodoItem)

    @Query(value = "UPDATE todo_item_table SET is_complete = :isCompleted_ WHERE item_id = :itemId_")
    suspend fun updateItemCompleted(itemId_: Long, isCompleted_: Boolean)

    @Query(value = "UPDATE todo_item_table SET item_name = :updatedName_ WHERE item_id = :itemId_")
    suspend fun updateItemName(itemId_: Long, updatedName_: String)

    /* Retrieving Item Queries */
    @Query(value = "SELECT * FROM todo_item_table WHERE item_Id = :itemId_")
    suspend fun getTodoItem(itemId_: Long): TodoItem?

    @Query(value = "SELECT * FROM todo_item_table WHERE list_id = :listId_")
    suspend fun getTodoItemsInList(listId_: Long): List<TodoItem>?

    @Query(value = "SELECT * FROM todo_item_table WHERE list_id = :listId_")
    fun observeTodoItemsInList(listId_: Long): LiveData<List<TodoItem>>

    /* Deleting Item Queries */
    @Query(value = "DELETE FROM todo_item_table WHERE item_id = :itemId_")
    suspend fun deleteItemById(itemId_: Long)
}
