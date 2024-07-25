package com.coolgirl.majko.Screen.Profile

data class ProfileUiState(
    val userName: String = "",
    val userEmail: String = "",
    val isChangePassword: Boolean = false,
    val oldPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
)
