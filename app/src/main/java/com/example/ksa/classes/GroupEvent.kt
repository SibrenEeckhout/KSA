package com.example.ksa.classes

import kotlinx.serialization.Serializable

@Serializable
data class GroupEvent(
    val id : Int,
    val title : String,
    val date : String,
    val groupId : Int
)
