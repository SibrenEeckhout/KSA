package com.example.ksa.ui

data class LoginUiState(
    val emailText: String = "",
    val passwordText: String = "",
    val isLoggedIn: Boolean = false,
    val error: String = "",
)