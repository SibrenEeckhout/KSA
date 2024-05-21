package com.example.ksa

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.ksa.classes.Todo
import com.example.ksa.data.TodoDao
import com.example.ksa.data.TodoDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.hasItem
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TodoDatabaseTest {

    private lateinit var todoDao: TodoDao
    private lateinit var db: TodoDatabase

    private val mockTodo = Todo("MockTodo")

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, TodoDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        todoDao = db.todoDao()
    }

    @After
    fun cleanup() {
        db.close()
    }

    @Test
    fun insertAndReadTodoItem() = runBlocking {
        todoDao.insert(mockTodo)

        val result = todoDao.getAllItems().first()

        assertThat(result, hasItem(mockTodo))
    }

    @Test
    fun deleteTodoItem() = runBlocking {
        todoDao.insert(mockTodo)

        todoDao.delete(mockTodo)

        val result = todoDao.getAllItems().first()

        assertThat(result, not(hasItem(mockTodo)))
    }
}
