package com.example.ksa.model

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.ksa.classes.Member
import com.example.ksa.ui.ContactListViewModel

class ContactPageModel {
    companion object {
        @Composable
        fun ContactItem(
            contact: Member,
            formattedFullName: String,
            formattedNickname: String,
            formattedPhoneNumber: String,
            formattedAddress: String,
            formattedEmail: String,
            onPhoneFieldClicked: () -> Unit,
            onMailFieldClicked: () -> Unit,
            onAddressFieldClicked: () -> Unit,
            contactGroupPicture : String
        ) {

            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .height(130.dp), elevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .padding(vertical = 10.dp)
                            .size(70.dp, 70.dp),
                        painter = rememberAsyncImagePainter(contact.profilePicture),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )

                    Divider(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(1.dp)
                            .padding(vertical = 16.dp)
                    )

                    Column(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .padding(vertical = 16.dp)
                            .weight(1f)
                    ) {
                        Row {
                            Text(
                                text = formattedFullName,
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .padding(bottom = 4.dp),
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = formattedNickname, fontSize = 14.sp,
                                fontStyle = FontStyle.Italic,
                            )
                        }
                        Text(
                            text = formattedEmail, fontSize = 14.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 2.dp)
                                .clickable(onClick = {
                                    onMailFieldClicked()
                                })
                        )
                        Text(
                            text = formattedPhoneNumber, fontSize = 14.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 2.dp)
                                .clickable {
                                    onPhoneFieldClicked()
                                }
                        )
                        Text(
                            text = formattedAddress, fontSize = 14.sp,
                            modifier = Modifier
                                .padding(bottom = 2.dp)
                                .fillMaxWidth()
                                .clickable {
                                    onAddressFieldClicked()
                                })
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(top = 4.dp),
                        verticalArrangement = Arrangement.Top
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(contactGroupPicture),
                            contentDescription = null,
                            Modifier
                                .size(30.dp, 30.dp)
                                .padding(end = 4.dp)
                        )
                    }
                }

            }
        }
    }
}

@Composable
fun ContactScreen(
    contactListViewModel: ContactListViewModel,
) {
    val contactUiState by contactListViewModel.uiState.collectAsState()
    contactListViewModel.updateMatchingMembers()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 50.dp)
    ) {
        Components.SearchBar(
            searchText = contactUiState.searchedName,
            onSearchTextChanged = { contactListViewModel.updateSearchText(it) },
            onGroupSelected = { contactListViewModel.updateSelectedGroup(it) },
            searchBarPlaceHolder = contactUiState.searchBarPlaceholder,
            groupNames = contactUiState.groupNames,
            onDropdownListClicked = { contactListViewModel.openDropdownList() },
            onDropdownListDismissed = { contactListViewModel.closeDropdownList() },
            isDropdownExpanded = contactUiState.isDropdownExpanded,
            selectedGroupName = contactUiState.selectedGroup
        )
        if  (contactUiState.foundContacts.isEmpty()) {
            Components.NoResults()
        }else{
            ContactList(
                contactListViewModel = contactListViewModel,
                membersByLetter = contactUiState.foundContacts
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContactList(
    contactListViewModel: ContactListViewModel,
    membersByLetter: Map<String, List<Member>>
) {
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        membersByLetter.forEach { (letter, members) ->
            stickyHeader {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.surface)
                        .padding(16.dp), verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = letter.uppercase(), fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                    Divider(
                       modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                    )
                }
            }
            items(members) { member ->
                ContactPageModel.ContactItem(
                    contact = member,
                    formattedFullName = contactListViewModel.formatFullName(member),
                    formattedNickname = contactListViewModel.formatNickname(member),
                    formattedPhoneNumber = contactListViewModel.formatPhoneNumber(member),
                    formattedEmail = contactListViewModel.formatEmail(member),
                    formattedAddress = contactListViewModel.formatAddress(member),
                    onMailFieldClicked = { contactListViewModel.sendMail(context, member) },
                    onPhoneFieldClicked = { contactListViewModel.dial(context, member) },
                    onAddressFieldClicked = { contactListViewModel.openInMaps(context, member) },
                    contactGroupPicture = contactListViewModel.getContactGroupPicture(member),
                )
            }
        }
    }
}