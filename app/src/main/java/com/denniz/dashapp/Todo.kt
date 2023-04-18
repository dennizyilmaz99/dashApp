package com.denniz.dashapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_list")
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
)

