package ru.irinavb.todolist.viewmodels

import android.app.Application
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.irinavb.todolist.R
import ru.irinavb.todolist.data.models.Priority
import ru.irinavb.todolist.data.models.ToDoData

class SharedViewModel(application: Application): AndroidViewModel(application)  {

    val emptyDatabase: MutableLiveData<Boolean> = MutableLiveData(true)

    fun checkIfDatabaseEmpty(dataList: List<ToDoData>) {
        emptyDatabase.value = dataList.isEmpty()
    }

    val listener: AdapterView.OnItemSelectedListener = object:
        AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?,
                                    view: View?, position: Int, id: Long) {
            when(position) {
                0 -> { (parent?.getChildAt(0) as? TextView)?.setTextColor(ContextCompat.getColor
                    (application, R.color.red)) }
                1 -> { (parent?.getChildAt(0) as? TextView)?.setTextColor(ContextCompat.getColor
                    (application, R.color.yellow)) }
                2 -> { (parent?.getChildAt(0) as? TextView)?.setTextColor(ContextCompat.getColor
                    (application, R.color.green)) }
            }
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {}
    }

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

    fun parsePriorityToInt(priority: Priority): Int {
        return when(priority) {
            Priority.HIGH -> 0
            Priority.MEDIUM -> 1
            Priority.LOW -> 2
        }
    }
}