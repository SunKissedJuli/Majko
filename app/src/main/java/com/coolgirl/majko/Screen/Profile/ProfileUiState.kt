package com.coolgirl.majko.Screen.Profile

import com.coolgirl.majko.R
import com.coolgirl.majko.Screen.TaskEditor.TaskEditorUiState
import com.coolgirl.majko.data.dataUi.User.CurrentUserDataResponseUi
import com.coolgirl.majko.data.remote.dto.User.CurrentUserDataResponse

data class ProfileUiState(
    val userName: String = DEFAULT_STRING,
    val userEmail: String = DEFAULT_STRING,
    val isChangePassword: Boolean = DEFAULT_BOOLEAN,
    val oldPassword: String = DEFAULT_STRING,
    val newPassword: String = DEFAULT_STRING,
    val confirmPassword: String = DEFAULT_STRING,
    val isError: Boolean = DEFAULT_BOOLEAN,
    val errorMessage: Int? = null,
    val currentUser: CurrentUserDataResponseUi? = null,
    val isMessage: Boolean = DEFAULT_BOOLEAN,
    val message: Int? = null
){
    companion object {
        const val DEFAULT_STRING = ""
        const val DEFAULT_BOOLEAN = false

        fun default() = ProfileUiState()
    }
}
