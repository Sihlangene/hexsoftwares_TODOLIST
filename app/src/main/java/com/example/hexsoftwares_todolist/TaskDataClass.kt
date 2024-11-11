package com.example.hexsoftwares_todolist

data class TaskDataClass(
    val id: Int,
    val date: String,
    val description: String,
    var isCompleted: Boolean = false
)