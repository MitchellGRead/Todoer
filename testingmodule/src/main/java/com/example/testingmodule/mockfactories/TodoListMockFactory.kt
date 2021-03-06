package com.example.testingmodule.mockfactories

import com.example.todoer.database.models.TodoList
import com.example.todoer.ui.createtodo.TodoType
import com.example.todoer.ui.homescreen.recycler.ChecklistItem
import com.example.todoer.utils.DateTimeUtils.getDateTimeString
import org.joda.time.DateTime

class TodoListMockFactory {

    private val listName = "listName"
    private val createdAt = DateTime(2020, 10, 10, 0, 0)
    private val listType = TodoType.toTodoType(TodoType.CheckListTypeId)
    private val completedTasks = 2
    private val totalTasks = 4

    val todoList1: TodoList
        get() = TodoList(
            listId = 1L,
            listName = listName,
            todoType = listType,
            createdAt = createdAt,
            editedAt = DateTime(2020, 10, 10, 23, 0),
            isFavourited = true,
            completedTasks = completedTasks,
            totalTasks = totalTasks
        )

    val todoList2: TodoList
        get() = TodoList(
            listId = 2L,
            listName = listName,
            todoType = listType,
            createdAt = createdAt,
            editedAt = DateTime(2020, 10, 10, 22, 0),
            isFavourited = false,
            completedTasks = completedTasks,
            totalTasks = totalTasks
        )

    val todoLists: List<TodoList>
        get() = listOf(
            todoList1,
            todoList2
        )

    val checkListItems: List<ChecklistItem>
        get() = todoLists.map { it.toHomeScreenItem() }

    companion object {
        fun TodoList.toHomeScreenItem(): ChecklistItem {
            val editedString = this.editedAt.getDateTimeString()
            return ChecklistItem(this, editedString)
        }
    }
}

