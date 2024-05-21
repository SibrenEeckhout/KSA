package com.example.ksa.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.ksa.KsaApplication
import com.example.ksa.classes.Announcement
import com.example.ksa.classes.Group
import com.example.ksa.data.KsaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AnnouncementListViewModel(
    private val ksaRepository: KsaRepository
) : ViewModel() {
    private val _uiState =
        MutableStateFlow(AnnouncementListUiState(allAnnouncements = ksaRepository.getAnnouncementsMatchingTargetGroup("ALL",""), groupNames = ksaRepository.getCurrentLoggedInMemberGroups()))
    val uiState: StateFlow<AnnouncementListUiState> = _uiState.asStateFlow()
    fun updateSearchText(it: String) {
        _uiState.update { currentState -> currentState.copy(searchedName = it) }
        updateMatchingAnnouncements()
    }

    fun getLatestGroupAnnouncement(groupName: String): Flow<Announcement> {
        val allAnnouncements = ksaRepository.getLatestGroupAnnouncement(groupName)
        return allAnnouncements.filter { announcement ->
            announcement.targetGroup.contains(groupName)
        }
    }

    fun updateSelectedGroup(it: String) {
        _uiState.update { currentState -> currentState.copy(selectedGroup = it) }
        updateMatchingAnnouncements()
    }

    fun updateMatchingAnnouncements() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                ksaRepository.getAnnouncementsMatchingTargetGroup(_uiState.value.selectedGroup, _uiState.value.searchedName).collect{ announcements ->
                 _uiState.update { currentState ->
                     currentState.copy(
                         foundAnnouncements = announcements
                     ) }
                }
            }
        }
    }

    fun getGroups(): Flow<List<Group>> {
        return ksaRepository.getGroups()
    }

    fun getAnnouncements(): Flow<List<Announcement>> {
        return ksaRepository.getAnnouncementsMatchingTargetGroup(_uiState.value.selectedGroup, _uiState.value.searchedName)
    }

    fun openDropdownList() {
        _uiState.update { currentState ->
            currentState.copy(
                isDropdownExpanded = true
            )
        }
    }

    fun closeDropdownList() {
        _uiState.update { currentState ->
            currentState.copy(
                isDropdownExpanded = false
            )
        }
    }

    fun getGroupPhoto(targetGroup: String, allGroups: List<Group>): String {
        val group = allGroups.find { it.name == targetGroup }
        return group?.groupPicture ?: "https://i.ibb.co/FnLkXMt/all.png"
    }

    fun getLatestAnnouncement(): Flow<Announcement> {
        return ksaRepository.getLatestAnnouncement()
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as KsaApplication)
                AnnouncementListViewModel(
                    KsaRepository(application.ksaDatabase.ksaDao()),
                )
            }
        }
    }
}