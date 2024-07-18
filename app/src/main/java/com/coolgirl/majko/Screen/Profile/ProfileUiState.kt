package com.coolgirl.majko.Screen.Profile

data class ProfileUiState(
    val userName: String = "",
    val userEmail: String = "",
    val is_adding: Boolean = false,
    val is_adding_background : Float = 1f,
    val oldPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
)
