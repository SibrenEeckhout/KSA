package com.example.ksa.data;

import com.example.ksa.classes.Todo

class TodoRepository(private val todoDao: TodoDao) {

    fun getTodos(): kotlinx.coroutines.flow.Flow<List<Todo>> {
        return todoDao.getAllItems()
    }

    fun addTodo(todo: Todo) {
        todoDao.insert(todo)
    }

    fun deleteTodo(todo: Todo) {
        todoDao.delete(todo)
    }
}
