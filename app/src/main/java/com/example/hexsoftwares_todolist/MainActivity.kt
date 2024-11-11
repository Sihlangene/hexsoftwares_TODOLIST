package com.example.hexsoftwares_todolist

import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.CalendarView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TaskAdapter
    private lateinit var dbHelper: TaskDatabaseHelper
    private var selectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calendarView = findViewById(R.id.calendarView)
        recyclerView = findViewById(R.id.recyclerViewTasks)
        recyclerView.layoutManager = LinearLayoutManager(this)

        dbHelper = TaskDatabaseHelper(this)

        // Initialize adapter with callback functions for delete and complete actions
        adapter = TaskAdapter(mutableListOf(), { task ->
            // Handle delete action safely
            try {
                dbHelper.deleteTask(task.id)
                Toast.makeText(this, "Task deleted!", Toast.LENGTH_SHORT).show()
                loadTasksForDate(selectedDate) // Reload tasks for the selected date after deletion
            } catch (e: Exception) {
                Toast.makeText(this, "Error deleting task: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }, { task ->
            // Handle complete action
            try {
                task.isCompleted = true
                dbHelper.updateTask(task)
                Toast.makeText(this, "Task marked as completed!", Toast.LENGTH_SHORT).show()
                loadTasksForDate(selectedDate) // Reload tasks for the selected date after updating
            } catch (e: Exception) {
                Toast.makeText(this, "Error updating task: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        })

        recyclerView.adapter = adapter

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = "$year-${month + 1}-$dayOfMonth"
            loadTasksForDate(selectedDate) // Load tasks for the selected date
        }

        findViewById<Button>(R.id.btnAddTask).setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setTitle("Add New Task")

            // Create an input layout
            val inputLayout = LinearLayout(this)
            inputLayout.orientation = LinearLayout.VERTICAL

            // Create EditText for task description input
            val inputDescription = EditText(this)
            inputDescription.hint = "Task Description"
            inputLayout.addView(inputDescription)

            // Set the input layout in the dialog
            dialogBuilder.setView(inputLayout)

            // Set up the dialog buttons
            dialogBuilder.setPositiveButton("Add") { _, _ ->
                val taskDescription = inputDescription.text.toString()
                if (taskDescription.isNotEmpty()) {
                    val newTask = TaskDataClass(0, selectedDate, taskDescription, false)
                    try {
                        dbHelper.addTask(newTask)
                        Toast.makeText(this, "Task added!", Toast.LENGTH_SHORT).show()
                        loadTasksForDate(selectedDate) // Reload tasks for the selected date after adding
                    } catch (e: Exception) {
                        Toast.makeText(this, "Error adding task: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Please enter a task description", Toast.LENGTH_SHORT).show()
                }
            }
            dialogBuilder.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }

            val dialog = dialogBuilder.create()
            dialog.show()
        }
    }

    private fun loadTasksForDate(date: String) {
        try {
            val tasks = dbHelper.getTasksByDate(date)
            adapter.updateTasks(tasks)
        } catch (e: Exception) {
            Toast.makeText(this, "Error loading tasks: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}

