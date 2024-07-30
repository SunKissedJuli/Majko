package com.coolgirl.majko.Screen.Login

data class LoginUiState(
    val userName: String = "",
    val userLogin: String = "",
    val userPassword: String = "",
    val userPasswordRepeat: String = "",
    val isError: Boolean = false,
    val errorMessage: Int? = null
)
