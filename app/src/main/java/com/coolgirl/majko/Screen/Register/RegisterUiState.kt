package com.coolgirl.majko.Screen.Register

import com.coolgirl.majko.R
import com.coolgirl.majko.Screen.TaskEditor.TaskEditorUiState

data class RegisterUiState(
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

        fun default() = RegisterUiState()
    }
}
