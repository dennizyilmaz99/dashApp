package com.denniz.dashapp

import androidx.room.*

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo_list")
    fun getAll(): List<TodoItem>

    @Insert
    fun insert(item: TodoItem)

    @Delete
    fun delete(item: TodoItem)

    @Update
    fun update(item: TodoItem)
}