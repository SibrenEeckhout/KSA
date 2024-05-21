package com.example.ksa.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.ksa.KsaApplication
import com.example.ksa.classes.Group
import com.example.ksa.classes.Member
import com.example.ksa.data.KsaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactListViewModel(
    private val ksaRepository: KsaRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ContactListUiState(ksaRepository))
    val uiState: StateFlow<ContactListUiState> = _uiState.asStateFlow()
    private var searchText by mutableStateOf("")
    private var selectedGroup by mutableStateOf("ALL")
    private val defaultGroupPicture = "https://i.ibb.co/JjMd42b/muts.png"
    private lateinit var allGroups: List<Group>

    init {
        viewModelScope.launch {
            ksaRepository.getGroups().collect { groups ->
                allGroups = groups
            }
        }
    }

    fun updateSearchText(it: String) {
        searchText = it
        _uiState.update { currentState -> currentState.copy(searchedName = searchText) }
        updateMatchingMembers()
    }

    fun updateSelectedGroup(it: String) {
        selectedGroup = it
        _uiState.update { currentState -> currentState.copy(selectedGroup = selectedGroup) }
        updateMatchingMembers()
    }

    fun updateMatchingMembers() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                ksaRepository.getMatchingMembers(_uiState.value.searchedName).collect { members ->
                   val filteredMembersByGroup = filterMembersByGroup(members)
                    _uiState.update { currentState ->
                        currentState.copy(
                            foundContacts = groupMembersByLetter(filteredMembersByGroup),
                        )
                    }
                }
            }
        }
    }

    private fun filterMembersByGroup(members: List<Member>): List<Member> {
        return if (selectedGroup == "ALL") {
            members
        } else {
            members.filter { member -> member.groups.contains(selectedGroup) }
        }
    }

    private fun groupMembersByLetter(listOfMembers: List<Member>): Map<String, List<Member>> {
        return listOfMembers.sortedBy { member -> member.firstName[0] }
            .groupBy { member -> member.firstName[0].toString() }
    }


    fun formatFullName(member: Member): String {
        return member.firstName + " " + member.lastName
    }

    fun formatNickname(member: Member): String {
        return "(" + member.nickName + ")"
    }

    fun formatEmail(member: Member): String {
        val mailEmojiUnicode = "\uD83D\uDCE7 "
        return mailEmojiUnicode + member.email
    }

    fun formatPhoneNumber(member: Member): String {
        val phoneEmojiUnicode = "\u260E "
        return phoneEmojiUnicode + member.phone
    }

    fun formatAddress(member: Member): String {
        val addressEmojiUnicode = "\uD83C\uDFE0 "
        return addressEmojiUnicode + member.address
    }

    // https://stackoverflow.com/questions/72731148/how-can-i-open-gmail-when-click-the-button-in-jetpack-compose
    fun sendMail(context: Context, member: Member) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "vnd.android.cursor.item/email"
        intent.putExtra(Intent.EXTRA_EMAIL, member.email)
        intent.putExtra(Intent.EXTRA_SUBJECT, "Onderwerp")
        context.startActivity(intent)
    }

    fun dial(context: Context, member: Member) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", member.phone, null))
        context.startActivity(intent)
    }

    fun openInMaps(context: Context, member: Member) {
        val gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(member.address))
        val intent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        intent.setPackage("com.google.android.apps.maps")
        context.startActivity(intent)
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

    fun getContactGroupPicture(member: Member): String {
        val group = allGroups.find { group -> group.name == member.groups[0] }
        return group?.groupPicture ?: defaultGroupPicture
    }


    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as KsaApplication)
                ContactListViewModel(
                    KsaRepository(application.ksaDatabase.ksaDao())
                )
            }
        }
    }
}

