package com.example.hexsoftwares_todolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private var tasks: MutableList<TaskDataClass>,
    private val onDelete: (TaskDataClass) -> Unit,
    private val onComplete: (TaskDataClass) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val taskDescription: TextView = view.findViewById(R.id.taskDescription)
        val checkBoxComplete: CheckBox = view.findViewById(R.id.checkBoxComplete)
        val btnDeleteTask: Button = view.findViewById(R.id.btnDeleteTask)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.taskDescription.text = task.description
        holder.checkBoxComplete.isChecked = task.isCompleted

        holder.checkBoxComplete.setOnCheckedChangeListener { _, isChecked ->
            onComplete(task.copy(isCompleted = isChecked))
        }

        holder.btnDeleteTask.setOnClickListener {
            onDelete(task)
        }
    }

    override fun getItemCount(): Int = tasks.size

    // Method to update the list of tasks
    fun updateTasks(newTasks: List<TaskDataClass>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }
}

