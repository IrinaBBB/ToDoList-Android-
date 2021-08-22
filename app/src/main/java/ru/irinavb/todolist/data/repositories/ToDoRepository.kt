package ru.irinavb.todolist.data.repositories

import androidx.lifecycle.LiveData
import ru.irinavb.todolist.data.ToDoDao
import ru.irinavb.todolist.data.models.ToDoData

class ToDoRepository(private val toDoDao: ToDoDao) {

    val getAllData: LiveData<List<ToDoData>> = toDoDao.getAllData()

    suspend fun insertData(toDoData: ToDoData) {
        toDoDao.insertData(toDoData)
    }
}