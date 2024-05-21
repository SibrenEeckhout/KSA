package com.example.ksa.classes

import kotlinx.serialization.Serializable

@Serializable
data class Document(
    val id : Int,
    val name : String,
    val url : String
)
