package com.example.ksa.model

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CheckBoxOutlineBlank
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.ksa.KsaScreen
import com.example.ksa.R
import com.example.ksa.classes.Group
import com.example.ksa.classes.Todo
import com.example.ksa.model.AnnouncementPage.Companion.AnnouncementItem
import com.example.ksa.ui.AnnouncementListViewModel
import com.example.ksa.ui.GroupViewModel
import com.example.ksa.ui.HomeListViewModel

class Home {

    companion object {
        @Composable
        fun HomePage(
            navController: NavHostController,
            homeListViewModel: HomeListViewModel,
            announcementListViewModel: AnnouncementListViewModel,
            groupViewModel: GroupViewModel,
        ) {
            
            LazyColumn(
                modifier = Modifier.padding(
                    start = 10.dp,
                    end = 10.dp,
                    top = 20.dp,
                    bottom = 40.dp
                )
            ) {
                item {
                    val latestAnnouncement = announcementListViewModel.getLatestAnnouncement().collectAsState(
                        initial = null
                    ).value
                    AnnouncementPage.AnnouncementTitleBar(navController = navController)
                    if (latestAnnouncement != null) {
                        AnnouncementItem(announcement = latestAnnouncement, cardPadding = 0.dp, announcementListViewModel)
                    }
                }
                item {
                    MyGroups(homeListViewModel = homeListViewModel, groupViewModel = groupViewModel, navController = navController)
                }
                item {
                    Box(modifier = Modifier.height(245.dp)) {
                        MyTodos(homeListViewModel)
                    }
                }
            }
        }

        @Composable
        private fun MyGroups(
            homeListViewModel: HomeListViewModel,
            groupViewModel: GroupViewModel,
            navController: NavHostController
        ) {
            val allGroups = homeListViewModel.getAllGroups().collectAsState(initial = emptyList()).value
            val currentLoggedInMember = homeListViewModel.getCurrentLoggedInMember().collectAsState(initial = null).value
            val myGroups = homeListViewModel.getMyGroups(allGroups, currentLoggedInMember)
            Text(
                text = stringResource(R.string.myGroups) + ":",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 5.dp, top = 40.dp)
            )
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, color = MaterialTheme.colors.secondary)
                    .padding(10.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                itemsIndexed(myGroups) { _, group ->
                    GroupCard(group = group, navController = navController, groupViewModel = groupViewModel)
                }
            }
        }

        @Composable
        private fun MyTodos(homeListViewModel: HomeListViewModel) {
            val todos = homeListViewModel.getTodos().collectAsState(initial = emptyList()).value
            val showDialog = remember { mutableStateOf(false) }
            val showDialogDelete = remember { mutableStateOf<Todo?>(null) }
            if (showDialog.value) {
                TodoDialog(
                    homeListViewModel = homeListViewModel,
                    onDismiss = { showDialog.value = false }
                )
            }

            if (showDialogDelete.value != null) {
                DeleteDialog(
                    homeListViewModel = homeListViewModel,
                    onDismiss = { showDialogDelete.value = null },
                    selectedTodo = showDialogDelete.value!!
                )
            }
            Column() {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp, top = 40.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = stringResource(R.string.myTodos) + ":", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Icon(
                        Icons.Rounded.Add,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.clickable { showDialog.value = true })
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(1.dp, color = MaterialTheme.colors.secondary)
                        .padding(10.dp)
                ) {
                    itemsIndexed(todos) { _, todo ->
                        Row(
                        ) {
                            Icon(
                                Icons.Rounded.CheckBoxOutlineBlank,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.clickable {

                                    showDialogDelete.value = todo
                                }
                            )
                            Text(
                                text = todo.message,
                                modifier = Modifier.padding(start = 5.dp, bottom = 10.dp),
                            )
                        }
                    }
                }
            }
        }

        @Composable
        private fun DeleteDialog(
            homeListViewModel: HomeListViewModel,
            onDismiss: () -> Unit,
            selectedTodo: Todo
        ) {

            AlertDialog(
                onDismissRequest = onDismiss,
                title = {
                    Box(
                        Modifier
                            .padding(bottom = 50.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = stringResource(R.string.markTodoAsDone))
                    }
                },
                text = {
                    Box(
                        Modifier
                            .padding(bottom = 30.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = selectedTodo.message)
                    }
                },

                dismissButton = {
                    TextButton(onClick = onDismiss) {
                        Text(text = stringResource(R.string.cancel))
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        homeListViewModel.removeTodo(selectedTodo)
                        onDismiss()
                    }) {
                        Text(
                            text = stringResource(R.string.confirm),
                        )
                    }

                })
        }

        @Composable
        fun TodoDialog(
            homeListViewModel: HomeListViewModel,
            onDismiss: () -> Unit
        ) {
            var todoText by remember { mutableStateOf("") }
            AlertDialog(
                onDismissRequest = onDismiss,
                title = {
                    Box(
                        Modifier
                            .padding(bottom = 30.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = stringResource(R.string.addTodo), modifier = Modifier.padding(bottom = 30.dp))
                        Text(text = "")
                    }
                },
                text = {
                    TextField(
                        value = todoText,
                        onValueChange = { todoText = it },
                        label = { Text(stringResource(R.string.todoExample)) }
                    )
                },
                modifier = Modifier,
                dismissButton = {
                    TextButton(
                        onClick =
                        onDismiss

                    ) {
                        Text(
                            text = stringResource(R.string.cancel),
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        onDismiss()
                        homeListViewModel.addTodo(todoText)
                    }) {
                        Text(
                            text = stringResource(R.string.add),
                        )
                    }
                })
        }

        @Composable
        fun GroupCard(group: Group, navController: NavHostController, groupViewModel: GroupViewModel) {
            Card(
                elevation = 4.dp,
                modifier = Modifier.clickable {
                    groupViewModel.setGroup(group)
                    navController.navigate(
                        KsaScreen.Group.name
                    )
                },
            ) {
                Image(
                    painter = rememberAsyncImagePainter(group.groupPicture),
                    contentDescription = null, modifier = Modifier
                        .size(100.dp)
                        .padding(10.dp)
                )
            }
        }
    }
}




