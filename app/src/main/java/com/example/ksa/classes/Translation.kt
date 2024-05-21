package com.example.ksa.classes

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "translations")
data class Translation(
    @PrimaryKey
    val id : Int,
    val announcementId: Int,
    val language: String,
    val title: String,
    val message: String,
)
