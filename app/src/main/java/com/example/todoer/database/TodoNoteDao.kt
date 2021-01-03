package com.example.todoer.database

import androidx.room.*
import com.example.todoer.database.models.TodoNote
import kotlinx.coroutines.flow.Flow
import org.joda.time.DateTime

@Dao
interface TodoNoteDao {

    /* Inserting Note Queries */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodoNote(todoNote: TodoNote): Long

    /* Updating Note Queries */
    @Query(value = "UPDATE todo_note_table SET note_name = :updatedName_ WHERE note_id = :noteId_")
    suspend fun updateNoteName(noteId_: Long, updatedName_: String)

    @Query(value = "UPDATE todo_note_table SET note_description = :updatedDescription_ WHERE note_id = :noteId_")
    suspend fun updateNoteDescription(noteId_: Long, updatedDescription_: String)

    @Query(value = "UPDATE todo_note_table SET edited_at = :editedDate_ WHERE note_id = :noteId_")
    suspend fun updateEditDate(noteId_: Long, editedDate_: DateTime)

    @Query(value = "UPDATE todo_note_table SET favourited = :isFavourited_ WHERE note_id = :noteId_")
    suspend fun updatedIsFavourited(noteId_: Long, isFavourited_: Boolean)

    /* Getting Note Queries */
    @Query(value = "SELECT * FROM todo_note_table WHERE note_id = :noteId_")
    suspend fun getTodoNote(noteId_: Long): TodoNote?

    @Query(value = "SELECT * FROM todo_note_table")
    fun observeTodoNotes(): Flow<List<TodoNote>>

    @Query(value = "SELECT note_description FROM todo_note_table WHERE note_id = :noteId_")
    suspend fun getNoteDescriptionById(noteId_: Long): String?

    /* Deleting List Queries */
    @Query(value = "DELETE FROM todo_note_table WHERE note_id = :noteId_")
    suspend fun deleteNoteById(noteId_: Long)

}
