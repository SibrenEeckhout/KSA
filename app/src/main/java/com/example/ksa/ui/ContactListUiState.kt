package com.example.ksa.ui

import com.example.ksa.classes.Member
import com.example.ksa.data.KsaRepository
import kotlinx.coroutines.flow.Flow

data class ContactListUiState(
    val searchedName: String = "",
    val foundContacts: Map<String, List<Member>> = emptyMap(),
    val searchBarPlaceholder: String =  "\uD83D\uDD0E  Zoeken",
    val groupNames: Flow<List<String>>,
    val isDropdownExpanded : Boolean = false,
    val selectedGroup: String? = "ALL"
){

    constructor(ksaRepository: KsaRepository) : this(groupNames = ksaRepository.getGroupNames())
}