package com.example.ksa.classes

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "announcements")
data class Announcement(
    @PrimaryKey
    val id : Int,
    val date: String,
    val targetGroup: String,
    val creatorId: Int,
    val translations: Map<String,Translation>
)
