package com.coolgirl.majko.Screen.Profile

import com.coolgirl.majko.data.remote.dto.User.CurrentUserDataResponse

data class ProfileUiState(
    val userName: String = "",
    val userEmail: String = "",
    val isChangePassword: Boolean = false,
    val oldPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isError: Boolean = false,
    val errorMessage: Int? = null,
    val currentUser: CurrentUserDataResponse? = null,
    val isMessage: Boolean = false,
    val message: Int? = null
)
