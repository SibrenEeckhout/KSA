package com.example.ksa.ui

import com.example.ksa.classes.Announcement
import kotlinx.coroutines.flow.Flow


data class AnnouncementListUiState(
    val searchedName: String = "",
    val selectedGroup: String = "ALL",
    val searchBarPlaceholder: String = "\uD83D\uDD0E  Zoeken",
    val isDropdownExpanded: Boolean = false,
    val allAnnouncements: Flow<List<Announcement>>,
    val foundAnnouncements: List<Announcement> = emptyList(),
    val groupNames: Flow<List<String>>,
)