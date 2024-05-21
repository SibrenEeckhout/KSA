package com.example.ksa.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.ksa.KsaApplication
import com.example.ksa.classes.Group
import com.example.ksa.classes.Member
import com.example.ksa.classes.Todo
import com.example.ksa.data.KsaRepository
import com.example.ksa.data.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeListViewModel(
    private val todoRepository: TodoRepository,
    private val ksaRepository: KsaRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeListUiState(todoRepository))
    val uiState: StateFlow<HomeListUiState> = _uiState.asStateFlow()

    fun addTodo(todoAsString: String) {
        val todo = Todo(todoAsString)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                todoRepository.addTodo(todo)
            }
        }
    }

    fun removeTodo(todo: Todo) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                todoRepository.deleteTodo(todo)
            }
        }
    }

    fun getTodos(): Flow<List<Todo>> {
        return _uiState.value.todos
    }

    fun getMyGroups(allGroups: List<Group>, currentLoggedInMember: Member?): List<Group> {
        if (allGroups.isEmpty() || currentLoggedInMember == null) return listOf()
        val myGroups = mutableListOf<Group>()
        allGroups.forEach { group ->
            group.members.forEach { member ->
                if (member.id == currentLoggedInMember.id) {
                    myGroups.add(group)
                }
            }
        }
        return myGroups
    }

    fun getAllGroups(): Flow<List<Group>> {
        return ksaRepository.getGroups()
    }

    fun getCurrentLoggedInMember(): Flow<Member> {
        return ksaRepository.getCurrentLoggedInMember()
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as KsaApplication)
                HomeListViewModel(
                    TodoRepository(application.todoDatabase.todoDao()),
                    KsaRepository(application.ksaDatabase.ksaDao()),
                )
            }
        }
    }
}