package com.example.ksa.classes
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "groups")
data class Group(
    @PrimaryKey
    val id : Int,
    val name : String,
    val groupPicture : String,
    val members : List<Member>,
    val documents : List<Document>,
    val activityResponsibles: List<Member>,
    val events : List<GroupEvent>
)
