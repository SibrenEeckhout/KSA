package com.example.ksa.ui

import com.example.ksa.classes.Announcement
import com.example.ksa.classes.Document
import com.example.ksa.classes.GroupEvent
import com.example.ksa.classes.Member
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class GroupUiState(
    val title: String = "",
    val documents: List<Document> = emptyList(),
    val activityResponsibles: List<Member> = emptyList(),
    val events: List<GroupEvent> = emptyList(),
    val latestGroupAnnouncement: Flow<Announcement> = emptyFlow()
)