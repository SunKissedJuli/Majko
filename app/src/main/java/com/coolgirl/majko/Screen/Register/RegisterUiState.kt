package com.coolgirl.majko.Screen.Register

data class RegisterUiState(
    val userName: String = "",
    val userLogin: String = "",
    val userPassword: String = "",
    val userPasswordRepeat: String = "",
    val isError: Boolean = false,
    val errorMessage: Int? = null
)
