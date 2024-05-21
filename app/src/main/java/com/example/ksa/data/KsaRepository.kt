package com.example.ksa.data

import com.example.ksa.classes.Announcement
import com.example.ksa.classes.CurrentMember
import com.example.ksa.classes.Group
import com.example.ksa.classes.Member
import com.example.ksa.classes.Translation
import kotlinx.coroutines.flow.Flow

class KsaRepository(private val ksaDao: KsaDao) {

    // Members
    fun addMember(member: Member) {
        ksaDao.insertMember(member)
    }
    fun getMatchingMembers(searchText: String): Flow<List<Member>> {
        return ksaDao.getMembersWithMatchingNames(searchText)
    }

    // Groups
    fun getGroups(): Flow<List<Group>> {
        return ksaDao.getAllGroups()
    }
    fun getGroupNames(): Flow<List<String>> {
        return ksaDao.getGroupsNames()
    }

    fun addGroup(group: Group) {
        ksaDao.insertGroup(group)
    }

    // Announcements
    fun getAnnouncementsMatchingTargetGroup(selectedGroup : String, searchText: String): Flow<List<Announcement>> {
        return ksaDao.getAnnouncementsMatchingSearch(selectedGroup, searchText)
    }

    fun addAnnouncement(announcement: Announcement) {
        ksaDao.insertAnnouncement(announcement)
    }

    fun getLatestGroupAnnouncement(targetGroup: String): Flow<Announcement> {
        return ksaDao.getLatestGroupAnnouncement(targetGroup)
    }

    fun getLatestAnnouncement(): Flow<Announcement> {
        return ksaDao.getLatestAnnouncement()
    }

    fun addTranslation(translation: Translation) {
        ksaDao.insertTranslation(translation)
    }

    // CurrentMember
    fun getCurrentLoggedInMember(): Flow<Member> {
        return ksaDao.getCurrentLoggedInMember()
    }

    fun login(member: CurrentMember) {
        ksaDao.insertCurrentLoggedInMember(member)
    }
    fun getCurrentLoggedInMemberGroups(): Flow<List<String>> {
        return  ksaDao.getCurrentLoggedInMemberGroups()
    }

    fun clearDatabase() {
        ksaDao.clearGroups()
        ksaDao.clearMembers()
        ksaDao.clearAnnouncements()
        ksaDao.clearTranslations()
    }

    fun getCurrentAccount(): Flow<CurrentMember> {
        return ksaDao.getCurrentAccount()
    }
}