package com.example.ksa.classes

import kotlinx.serialization.Serializable

@Serializable
data class LoginData (
    val email: String,
    val password: String
)