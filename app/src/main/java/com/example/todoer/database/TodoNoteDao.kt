package com.example.todoer.database

import androidx.room.*
import com.example.todoer.database.models.TodoNote
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoNoteDao {

    /* Inserting Note Queries */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodoNote(todoNote: TodoNote): Long

    /* Updating Note Queries */
    @Update
    suspend fun updateTodoNote(todoNote: TodoNote)

    @Query(value = "UPDATE todo_note_table SET note_name = :updatedName_ WHERE note_id = :noteId_")
    suspend fun updateNoteName(noteId_: Long, updatedName_: String)

    @Query(value = "UPDATE todo_note_table SET note_description = :updatedDescription_ WHERE note_id = :noteId_")
    suspend fun updateNoteDescription(noteId_: Long, updatedDescription_: String)

    /* Getting Note Queries */
    @Query(value = "SELECT * FROM todo_note_table WHERE note_id = :noteId_")
    suspend fun getTodoNote(noteId_: Long): TodoNote?

    @Query(value = "SELECT * FROM todo_note_table")
    fun observeTodoNotes(): Flow<List<TodoNote>>

    @Query(value = "SELECT * FROM todo_note_table")
    suspend fun getTodoNotes(): List<TodoNote>?

    /* Deleting List Queries */
    @Query(value = "DELETE FROM todo_note_table WHERE note_id = :noteId_")
    suspend fun deleteNoteById(noteId_: Long)
}
