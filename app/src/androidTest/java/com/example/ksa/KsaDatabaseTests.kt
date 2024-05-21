package com.example.ksa

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.ksa.classes.CurrentMember
import com.example.ksa.classes.Document
import com.example.ksa.classes.Group
import com.example.ksa.classes.GroupEvent
import com.example.ksa.classes.Member
import com.example.ksa.data.KsaDao
import com.example.ksa.data.KsaDatabase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.hasItem
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class KsaDatabaseTests {

    private lateinit var dao: KsaDao
    private lateinit var db: KsaDatabase

    private val mockMember = Member(1,"Mock", "Member", "", "", "", "", "Leider", emptyList(), "")
    private val mockDocument = Document(1, "MockDocument", "Document");
    private val mockGroupEvent = GroupEvent(1, "MockGroupEvent", "GroupEvent", 1)
    private val mockGroup = Group(1, "MockGroup", "Group", listOf(mockMember), listOf(mockDocument), listOf(mockMember),
        listOf(mockGroupEvent))
    private val mockAccount = CurrentMember(mockMember.id)
    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, KsaDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.ksaDao()
    }

    @After
    fun cleanup() {
        db.close()
    }

    @Test
    fun insertAndReadMember() = runBlocking {
        dao.insertMember(mockMember)

        val memberSize = dao.getMembersWithMatchingNames("Mock").first().size
        assertEquals(1, memberSize)
    }
    @Test
    fun deleteMember() = runBlocking {
        dao.insertMember(mockMember)

        dao.clearMembers()

        val result = dao.getMembersWithMatchingNames("").first()

        assertThat(result, not(hasItem(mockMember)))
    }

    @Test
    fun insertAndReadGroup() = runBlocking {
        dao.insertGroup(mockGroup)

        val groupSize = dao.getAllGroups().first().size
        assertEquals(1, groupSize)
    }

    @Test
    fun insertAndReadGroupWithMembers() = runBlocking {
        dao.insertGroup(mockGroup)

        val group = dao.getAllGroups().first()
        val members = group[0].members.size
        assertEquals(1, members)
    }

    @Test
    fun deleteGroup() = runBlocking {
        dao.insertGroup(mockGroup)

        dao.clearGroups()

        val result = dao.getAllGroups().first()

        assertThat(result, not(hasItem(mockGroup)))
    }

    @Test
    fun getAllGroupNames() = runBlocking {
        dao.insertGroup(mockGroup)

        val groupNames = dao.getGroupsNames().first()
        assertEquals("MockGroup", groupNames[0])
    }

    @Test
    fun readDocumentFromGroup() = runBlocking{
        dao.insertGroup(mockGroup)

        val group = dao.getAllGroups().first()
        val documents = group[0].documents.size
        assertEquals(1, documents)
    }

    @Test
    fun readGroupEventFromGroup() = runBlocking{
        dao.insertGroup(mockGroup)

        val group = dao.getAllGroups().first()
        val groupEvents = group[0].events.size
        assertEquals(1, groupEvents)
    }

    @Test
    fun readActivityResponsibleFromGroup() = runBlocking{
        dao.insertGroup(mockGroup)

        val group = dao.getAllGroups().first()
        val activityResponsible = group[0].activityResponsibles.size
        assertEquals(1, activityResponsible)
    }

    @Test
    fun insertAndReadCurrentLoggedInMember() = runBlocking {
        dao.insertCurrentLoggedInMember(mockAccount)
        dao.insertMember(mockMember)

        val currentLoggedInMember = dao.getCurrentLoggedInMember().first()
        assertEquals(1, currentLoggedInMember.id)
    }
}
