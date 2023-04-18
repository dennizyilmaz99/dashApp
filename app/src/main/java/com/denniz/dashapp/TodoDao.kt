package com.denniz.dashapp

import androidx.room.*

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo_list")
    fun getAll(): List<Todo>

    @Insert
    fun insert(item: Todo)

    @Delete
    fun delete(item: Todo)

    @Update
    fun update(item: Todo)
}