package com.coolgirl.majko.Screen.Login

import com.coolgirl.majko.Screen.Project.ProjectUiState

data class LoginUiState(
    val userName: String = DEFAULT_STRING,
    val userLogin: String = DEFAULT_STRING,
    val userPassword: String = DEFAULT_STRING,
    val userPasswordRepeat: String = DEFAULT_STRING,
    val isError: Boolean = DEFAULT_BOOLEAN,
    val errorMessage: Int? = null
){
    companion object {
        const val DEFAULT_STRING = ""
        const val DEFAULT_BOOLEAN = false

        fun default() = LoginUiState()
    }
}
