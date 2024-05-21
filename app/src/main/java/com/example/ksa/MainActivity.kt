package com.example.ksa

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Surface
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.ContactPage
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ksa.model.*
import com.example.ksa.model.AnnouncementPage.Companion.AnnouncementPage
import com.example.ksa.model.GroupPage.Companion.GroupPage
import com.example.ksa.model.Home.Companion.HomePage
import com.example.ksa.model.Login.Companion.LoginPage
import com.example.ksa.ui.*
import com.example.ksa.ui.theme.KsaTheme

enum class KsaScreen {
    DashBoard,
    Contact,
    Announcement,
    Group,
    Login
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KsaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    KsaApp()
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun KsaApp(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = viewModel(factory = LoginViewModel.factory),
    homeListViewModel: HomeListViewModel = viewModel(factory = HomeListViewModel.factory),
    contactListViewModel: ContactListViewModel = viewModel(factory = ContactListViewModel.factory),
    announcementListViewModel: AnnouncementListViewModel = viewModel(factory = AnnouncementListViewModel.factory),
    groupViewModel: GroupViewModel = viewModel(factory = GroupViewModel.factory)
) {
    var topBarText by remember { mutableStateOf("Home") }
    val navController = rememberNavController()
    val currentAccount = loginViewModel.getCurrentAccount().collectAsState(initial = null).value
    var startScreen = KsaScreen.Login.name

    if (currentAccount != null) {
        startScreen = KsaScreen.DashBoard.name
        loginViewModel.getApiData(currentAccount.id)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBar(topBarText) },
        bottomBar = {
            if  (currentAccount != null) {
                BottomBar(navController)
            }
        }) {
        NavHost(
            navController = navController,
            startDestination = startScreen,
            modifier = modifier
        ) {
            composable(route = KsaScreen.Login.name) {
                topBarText = stringResource(R.string.login) + ":"
                LoginPage(loginViewModel = loginViewModel)
            }
            composable(route = KsaScreen.DashBoard.name) {
                topBarText = stringResource(R.string.dashboard) + ":"
                HomePage(
                    navController = navController,
                    homeListViewModel,
                    announcementListViewModel,
                    groupViewModel
                )
            }
            composable(route = KsaScreen.Contact.name) {
                topBarText = stringResource(R.string.contacts) + ":"
                ContactScreen(contactListViewModel)
            }
            composable(route = KsaScreen.Group.name) {
                val groupUiState =
                    groupViewModel.uiState.collectAsState(initial = GroupUiState()).value
                topBarText = groupUiState.title
                GroupPage(
                    navController = navController,
                    announcementListViewModel,
                    groupViewModel
                )
            }
            composable(route = KsaScreen.Announcement.name) {
                topBarText = stringResource(R.string.announcements) + ":"
                AnnouncementPage(announcementListViewModel = announcementListViewModel)
            }
        }
    }
}

// TOPBAR
@Composable
fun TopAppBar(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp), contentAlignment = Alignment.Center
    ) {
        Text(text = title, fontSize = 25.sp, fontWeight = FontWeight.Bold)
    }
}

//BOTTOM BAR
@Composable
fun BottomBar(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp), contentAlignment = Alignment.Center
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, end = 30.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    Icons.Rounded.ContactPage,
                    contentDescription = null,
                    tint = MaterialTheme.colors.secondary,
                    modifier = Modifier
                        .size(45.dp)
                        .clickable { navController.navigate(KsaScreen.Contact.name) }
                )
                Icon(
                    Icons.Rounded.Home,
                    contentDescription = null,
                    tint = MaterialTheme.colors.secondary,
                    modifier = Modifier
                        .size(45.dp)
                        .clickable { navController.navigate(KsaScreen.DashBoard.name) }
                )
                Icon(
                    Icons.Rounded.Notifications,
                    contentDescription = null,
                    tint = MaterialTheme.colors.secondary,
                    modifier = Modifier
                        .size(45.dp)
                        .clickable { navController.navigate(KsaScreen.Announcement.name) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    KsaTheme {
        KsaApp()
    }
}