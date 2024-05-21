package com.example.ksa.data

import androidx.room.*
import com.example.ksa.classes.Todo
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Query("SELECT * from todos")
    fun getAllItems(): Flow<List<Todo>>
    @Upsert
    fun insert(todo: Todo)
    @Delete
    fun delete(todo: Todo)
}