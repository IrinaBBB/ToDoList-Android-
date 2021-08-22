package ru.irinavb.todolist.fragments.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import ru.irinavb.todolist.R
import ru.irinavb.todolist.data.models.Priority
import ru.irinavb.todolist.data.models.ToDoData
import ru.irinavb.todolist.databinding.RowLayoutBinding

class ToDoAdapter: RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {

    var dataList = emptyList<ToDoData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        return ToDoViewHolder(RowLayoutBinding.inflate(LayoutInflater.from(parent.context),
            parent, false))
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.binding.titleText.text = dataList[position].title
        holder.binding.descriptionText.text = dataList[position].description
        holder.binding.rowBackground.setOnClickListener {
            val action = ListFragmentDirections
                .actionListFragmentToUpdateFragment(dataList[position])
            holder.itemView.findNavController().navigate(action)
        }

        when(dataList[position].priority) {
            Priority.HIGH -> holder.binding.priorityIndicator
                .setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color
                    .red))
            Priority.MEDIUM -> holder.binding.priorityIndicator
                .setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color
                    .yellow))
            Priority.LOW -> holder.binding.priorityIndicator
                .setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color
                    .green))
        }
    }

    override fun getItemCount() = dataList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(toDoData: List<ToDoData>) {
        this.dataList = toDoData
        this.notifyDataSetChanged()
    }

    inner class ToDoViewHolder(val binding: RowLayoutBinding)
        :RecyclerView.ViewHolder(binding.root)
}