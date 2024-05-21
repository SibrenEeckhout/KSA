package com.example.ksa.classes

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "members")
data class Member(
    @PrimaryKey
    val id : Int,
    val firstName: String,
    val lastName: String,
    val nickName: String,
    val address: String,
    val email: String,
    val phone: String,
    val rank: String,
    val groups: List<String>,
    val profilePicture : String
)
