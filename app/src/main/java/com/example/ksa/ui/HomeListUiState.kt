package com.example.ksa.ui

import com.example.ksa.classes.Todo
import com.example.ksa.data.TodoRepository
import kotlinx.coroutines.flow.Flow

data class HomeListUiState(
    val todos: Flow<List<Todo>>
) {
    constructor(todoRepository: TodoRepository) : this(todoRepository.getTodos())
}
