package com.coolgirl.majko.Screen.Profile

data class ProfileUiState(
    val userName: String = "",
    val userEmail: String = "",
    val isAdding: Boolean = false,
    val isAddingBackground : Float = 1f,
    val oldPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
)
