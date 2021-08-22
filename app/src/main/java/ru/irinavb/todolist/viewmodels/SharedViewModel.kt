package ru.irinavb.todolist.viewmodels

import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.AndroidViewModel
import ru.irinavb.todolist.data.models.Priority

class SharedViewModel(application: Application): AndroidViewModel(application)  {

    fun verifyDataFromUser(title: String, description: String): Boolean {
        return !(TextUtils.isEmpty(title) || TextUtils.isEmpty(description))
    }

    fun parsePriority(priority: String): Priority {
        return when(priority) {
            "High Priority" -> Priority.HIGH
            "Medium Priority" -> Priority.MEDIUM
            "Low Priority" -> Priority.LOW
            else -> Priority.LOW
        }
    }
}