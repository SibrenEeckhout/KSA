package com.example.ksa.model

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.ksa.KsaScreen
import com.example.ksa.R
import com.example.ksa.classes.Announcement
import com.example.ksa.ui.AnnouncementListViewModel

class AnnouncementPage {
    companion object {
        @Composable
        fun AnnouncementItem(
            announcement: Announcement,
            cardPadding: Dp,
            announcementListViewModel: AnnouncementListViewModel = viewModel()
        ) {
            val allGroups =
                announcementListViewModel.getGroups().collectAsState(initial = emptyList()).value

            Card(
                modifier = Modifier
                    .padding(cardPadding)
                    .fillMaxWidth()
                    .border(1.dp, color = MaterialTheme.colors.secondary)
            ) {
                Column(modifier = Modifier.padding(15.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Rounded.Circle,
                                contentDescription = null,
                                modifier = Modifier.size(15.dp),
                                tint = MaterialTheme.colors.secondary
                            )
                            Text(
                                text = announcement.date,
                                modifier = Modifier.padding(start = 5.dp)
                            )
                        }
                        Image(
                            painter = rememberAsyncImagePainter(
                                announcementListViewModel.getGroupPhoto(
                                    announcement.targetGroup,
                                    allGroups
                                )
                            ),
                            contentDescription = null,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                    Text(
                        text = announcement.translations["nl"]?.title ?: "",
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp,
                        modifier = Modifier.padding(bottom = 10.dp, start = 2.dp)
                    )
                    Text(
                        text = announcement.translations["nl"]?.message ?: "",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 2.dp)
                    )
                }
            }
        }

        @Composable
        fun AnnouncementTitleBar(navController: NavHostController) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = stringResource(R.string.recentAnnouncements) + ":",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    text = stringResource(R.string.seeAll),
                    fontSize = 15.sp,
                    modifier = Modifier.clickable {
                        navController.navigate(
                            KsaScreen.Announcement.name
                        )
                    }
                )
            }
        }

        @Composable
        fun AnnouncementPage(
            announcementListViewModel: AnnouncementListViewModel
        ) {
            val announcementUiState by announcementListViewModel.uiState.collectAsState()

            announcementListViewModel.updateMatchingAnnouncements()

            Components.SearchBar(
                searchText = announcementUiState.searchedName,
                onSearchTextChanged = {
                    announcementListViewModel.updateSearchText(it)
                },
                onGroupSelected = {
                    announcementListViewModel.updateSelectedGroup(it)
                },
                searchBarPlaceHolder = announcementUiState.searchBarPlaceholder,
                groupNames = announcementUiState.groupNames,
                onDropdownListClicked = { announcementListViewModel.openDropdownList() },
                onDropdownListDismissed = { announcementListViewModel.closeDropdownList() },
                isDropdownExpanded = announcementUiState.isDropdownExpanded,
                selectedGroupName = announcementUiState.selectedGroup
            )

            if (announcementUiState.foundAnnouncements.isNotEmpty()) {
                LazyColumn(modifier = Modifier.padding(bottom = 45.dp, top = 45.dp)) {
                    itemsIndexed(announcementUiState.foundAnnouncements) { _, announcement ->
                        AnnouncementItem(
                            announcement = announcement,
                            cardPadding = 10.dp,
                            announcementListViewModel = announcementListViewModel
                        )
                    }
                }
            } else {
                Components.NoResults()
            }
        }
    }
}