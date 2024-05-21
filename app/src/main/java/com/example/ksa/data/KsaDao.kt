package com.example.ksa.data

import androidx.room.*
import com.example.ksa.classes.Announcement
import com.example.ksa.classes.CurrentMember
import com.example.ksa.classes.Group
import com.example.ksa.classes.Member
import com.example.ksa.classes.Translation
import kotlinx.coroutines.flow.Flow

@Dao
interface KsaDao {

    // Members
    @Query("SELECT * from members where firstName LIKE '%' || :searchText || '%' OR lastName LIKE '%' || :searchText || '%' OR nickName LIKE '%' || :searchText || '%' or :searchText = ''")
    fun getMembersWithMatchingNames(searchText: String): Flow<List<Member>>

    // Announcements
    @Query("select * from translations t join announcements a on t.announcementId = a.id where language = 'nl' AND (t.title LIKE '%' || :searchText || '%' OR t.message LIKE '%' || :searchText || '%' OR :searchText = '') and (a.targetGroup = :selectedGroup or :selectedGroup = 'ALL') ")
    fun getAnnouncementsMatchingSearch(selectedGroup: String, searchText: String): Flow<List<Announcement>>

    @Query("SELECT * FROM announcements WHERE targetGroup = :targetGroup ORDER BY id DESC LIMIT 1")
    fun getLatestGroupAnnouncement(targetGroup : String): Flow<Announcement>

    @Query("SELECT * FROM announcements ORDER BY id DESC LIMIT 1")
    fun getLatestAnnouncement(): Flow<Announcement>

    // Groups
    @Query("SELECT * from groups")
    fun getAllGroups(): Flow<List<Group>>

    @Query("SELECT * from groups where name = :name")
    fun getGroupByName(name: String): Flow<Group>

    @Query("SELECT name from groups")
    fun getGroupsNames(): Flow<List<String>>

    // CurrentMember
    @Query("SELECT * from members where id = (select id from currentAccount) limit 1")
    fun getCurrentLoggedInMember(): Flow<Member>

    @Query("SELECT groups from members where id = (select id from currentAccount) limit 1")
    fun getCurrentLoggedInMemberGroups(): Flow<List<String>>
    
    //Update tables
    @Upsert
    fun insertAnnouncement(announcement: Announcement)
    @Upsert
    fun insertCurrentLoggedInMember(member: CurrentMember)
    @Upsert
    fun insertTranslation(translation: Translation)
    @Upsert
    fun insertGroup(group: Group)
    @Upsert
    fun insertMember(member: Member)


    // clear tables
    @Query("DELETE FROM members")
    fun clearMembers()
    @Query("DELETE FROM groups")
    fun clearGroups()
    @Query("DELETE FROM announcements")
    fun clearAnnouncements()
    @Query("DELETE FROM translations")
    fun clearTranslations()

    @Query("select * from currentAccount")
     fun getCurrentAccount(): Flow<CurrentMember>
}