package com.example.ksa.model

import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ksa.R
import com.example.ksa.classes.Announcement
import com.example.ksa.classes.Document
import com.example.ksa.classes.Member
import com.example.ksa.ui.AnnouncementListViewModel
import com.example.ksa.ui.GroupViewModel
import com.pushpal.jetlime.data.JetLimeItemsModel
import com.pushpal.jetlime.data.config.*
import com.pushpal.jetlime.ui.JetLimeView

class GroupPage() {

    companion object {
        @Composable
        fun GroupPage(
            navController: NavHostController,
            announcementListViewModel: AnnouncementListViewModel,
            groupViewModel: GroupViewModel
        ) {
            val latestGroupAnnouncement = announcementListViewModel.getLatestGroupAnnouncement(groupViewModel.getGroupName()).collectAsState(
                initial = null
            ).value
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 10.dp,
                        end = 10.dp,
                        top = 20.dp,
                        bottom = 20.dp
                    )
            ) {
                FirstRow(groupViewModel = groupViewModel)
                Timeline(groupViewModel = groupViewModel)
                Spacer(modifier = Modifier.padding(top = 20.dp))
                RecentAnnouncement(
                    navController = navController,
                    announcementListViewModel = announcementListViewModel,
                    recentGroupAnnouncement = latestGroupAnnouncement
                )
            }
        }

        @Composable
        private fun RecentAnnouncement(
            navController: NavHostController,
            announcementListViewModel: AnnouncementListViewModel,
            recentGroupAnnouncement : Announcement?
        ) {
            AnnouncementPage.AnnouncementTitleBar(navController = navController)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            ) {
                if (recentGroupAnnouncement != null){
                    AnnouncementPage.AnnouncementItem(
                        recentGroupAnnouncement,
                        cardPadding = 0.dp,
                        announcementListViewModel = announcementListViewModel
                    )
                }else{
                    Card(
                        modifier = Modifier
                            .padding(0.dp)
                            .fillMaxWidth()
                            .border(1.dp, color = MaterialTheme.colors.secondary)
                    ){
                        Text(text = stringResource(R.string.noResults), modifier = Modifier.padding(10.dp))
                    }
                }

            }
        }


        @OptIn(ExperimentalAnimationApi::class)
        @Composable
        private fun Timeline(groupViewModel: GroupViewModel) {

            val jetTimeLineViewConfig = JetLimeViewConfig(
                backgroundColor = MaterialTheme.colors.background,
                lineColor = MaterialTheme.colors.secondary,
                lineThickness = 12F,
                lineType = LineType.Solid,
                lineStartMargin = 48.dp,
                lineEndMargin = 36.dp,
                iconSize = 26.dp,
                iconShape = CircleShape,
                iconBorderThickness = 2.dp,
                itemSpacing = 0.dp,
                showIcons = true
            )
            val jetTimeItemConfig = JetLimeItemConfig(
                iconColor = MaterialTheme.colors.onSurface,
                descriptionColor = MaterialTheme.colors.onSurface,
                titleColor = MaterialTheme.colors.onSurface
            )
            // make a list of JetLimeItemsModel
            val jetLimeItemsModel = remember {
                JetLimeItemsModel(
                    list = mutableStateListOf()
                )
            }
            if (jetLimeItemsModel.items.size != groupViewModel.getEvents().size) {
                groupViewModel.getEvents().forEach {
                    jetLimeItemsModel.addItem(
                        JetLimeItemsModel.JetLimeItem(
                            title = it.title,
                            description = it.date,
                            jetLimeItemConfig = jetTimeItemConfig,
                        )
                    )
                }
            }

            Text(
                text = stringResource(R.string.timeline) + ":", fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 10.dp),
            )
            Column(
                modifier = Modifier
                    .border(1.dp, color = MaterialTheme.colors.secondary)
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                JetLimeView(
                    jetLimeItemsModel = jetLimeItemsModel,
                    jetLimeViewConfig = jetTimeLineViewConfig,
                )
            }
        }

        @Composable
        private fun FirstRow(groupViewModel: GroupViewModel) {
            val groupUiState by groupViewModel.uiState.collectAsState()
            val documents = groupUiState.documents
            val activityResponsibles = groupUiState.activityResponsibles
            val context = LocalContext.current
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DocumentsList(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 5.dp)
                        .fillMaxHeight(),
                    documents = documents,
                    groupViewModel = groupViewModel,
                    context = context
                )
                GroupResponsibleList(
                    activityResponsibles = activityResponsibles,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 5.dp)
                        .fillMaxHeight()
                )
            }

        }

        @Composable
        fun DocumentItem(name: String, onclick: () -> Unit) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onclick)
                    .padding(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Outlined.Description,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.padding(end = 5.dp)
                )
                Text(text = name, fontSize = 12.sp)
            }
            HorizontalDivider(Color.Gray, 5.dp, 0.5.dp)
        }

        @Composable
        fun ActivityResponsibleItem(name: String) {
            Row(
                modifier = Modifier
                    .padding(5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    Icons.Outlined.Person,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.padding(end = 5.dp)
                )
                Text(text = name, fontSize = 12.sp)
            }
            HorizontalDivider(Color.Gray, 0.dp, 0.5.dp)
        }

        @Composable
        fun DocumentsList(
            documents: List<Document>,
            groupViewModel: GroupViewModel,
            context: Context,
            modifier: Modifier
        ) {
            Column(
                modifier = modifier
            ) {
                Text(
                    text = stringResource(R.string.documents) + ":", fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 10.dp),
                )

                LazyColumn(
                    modifier = Modifier
                        .border(1.dp, color = MaterialTheme.colors.secondary)
                ) {
                    if (documents.isEmpty()) {
                        item {
                            Text(
                                text = stringResource(R.string.noResults) + ":",
                                fontSize = 12.sp,
                                modifier = Modifier
                                    .padding(5.dp)
                                    .fillMaxWidth()
                            )
                        }
                    } else {
                        itemsIndexed(documents) { _, document ->
                            DocumentItem(
                                name = document.name,
                                onclick = {
                                    groupViewModel.openInGoogleDrive(
                                        context,
                                        document.url
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }

        @Composable
        fun GroupResponsibleList(activityResponsibles: List<Member>, modifier: Modifier) {
            Column(
                modifier = modifier
            ) {
                Text(
                    text = "Spelmakers:", fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 10.dp),
                )
                LazyColumn(
                    modifier = Modifier
                        .border(1.dp, color = MaterialTheme.colors.secondary)
                ) {
                    if (activityResponsibles.isEmpty()) {
                        item {
                            Text(
                                text = "Geen spelmakers",
                                fontSize = 12.sp,
                                modifier = Modifier
                                    .padding(5.dp)
                                    .fillMaxWidth()
                            )
                        }
                    } else {
                        itemsIndexed(activityResponsibles) { _, activityResponsible ->
                            ActivityResponsibleItem(
                                name = activityResponsible.firstName + " " + activityResponsible.lastName,
                            )
                        }
                    }
                }
            }
        }

        @Composable
        fun HorizontalDivider(color: Color, padding: Dp, height: Dp) {
            Divider(
                color = color,
                modifier = Modifier
                    .padding(horizontal = padding)
                    .fillMaxWidth()
                    .height(height)
            )
        }
    }
}