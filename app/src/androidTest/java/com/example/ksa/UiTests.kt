package com.example.ksa

import android.content.Context
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.ksa.classes.CurrentMember
import com.example.ksa.classes.Document
import com.example.ksa.classes.Group
import com.example.ksa.classes.GroupEvent
import com.example.ksa.classes.Member
import com.example.ksa.data.KsaDao
import com.example.ksa.data.KsaDatabase
import com.example.ksa.data.KsaMockDatabase
import com.example.ksa.ui.theme.KsaTheme
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UiTest {
    private lateinit var dao: KsaDao
    private lateinit var db: KsaMockDatabase

    private val mockMember = Member(1,"Mock", "Member", "", "", "", "", "Leider", emptyList(), "")
    private val mockDocument = Document(1, "MockDocument", "Document");
    private val mockGroupEvent = GroupEvent(1, "MockGroupEvent", "GroupEvent", 1)
    private val mockGroup = Group(1, "MockGroup", "Group", listOf(mockMember), listOf(mockDocument), listOf(mockMember),
        listOf(mockGroupEvent))
    private val mockAccount = CurrentMember(mockMember.id)
    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        // use a different database name for the test database
        db = Room.inMemoryDatabaseBuilder(context, KsaMockDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.ksaDao()
    }

    @After
    fun cleanup() {
        db.close()
    }

    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun showLoginScreen(){
        composeTestRule.setContent {
            KsaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    KsaApp()
                }
            }
        }
        composeTestRule.onNodeWithText("Login").assertExists()
    }

    @Test
    fun showDashboardScreenWhenLoggedIn() {
        // insert a mock user in the test database
        dao.insertCurrentLoggedInMember(mockAccount)

        composeTestRule.setContent {
            KsaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    KsaApp()
                }
            }
        }
        // assert that the Dashboard screen is displayed
        Thread.sleep(10000)
        composeTestRule.onNodeWithText("Dashboard").assertExists()
    }

}