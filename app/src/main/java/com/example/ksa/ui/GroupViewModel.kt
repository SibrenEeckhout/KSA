package com.example.ksa.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.ksa.KsaApplication
import com.example.ksa.classes.Group
import com.example.ksa.classes.GroupEvent
import com.example.ksa.data.KsaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GroupViewModel(
    private val ksaRepository: KsaRepository
) : ViewModel() {
    private lateinit var group: Group

    private val _uiState = MutableStateFlow(GroupUiState())
    val uiState: StateFlow<GroupUiState> = _uiState.asStateFlow()

    fun setGroup(group: Group) {
        this.group = group
        _uiState.update { it.copy(title = group.name) }
        _uiState.update { it.copy(documents = group.documents) }
        _uiState.update { it.copy(activityResponsibles = group.activityResponsibles) }
        _uiState.update  { it.copy(latestGroupAnnouncement = ksaRepository.getLatestGroupAnnouncement(group.name)) }
    }

    fun getGroupName(): String {
        return group.name
    }

    fun openInGoogleDrive(context: Context, url : String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        context.startActivity(intent)
    }


    fun getEvents(): List<GroupEvent> {
        return group.events.sortedBy { it.date }
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as KsaApplication)
                GroupViewModel(
                    KsaRepository(application.ksaDatabase.ksaDao())
                )
            }
        }
    }
}