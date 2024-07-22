package com.coolgirl.majko.data.remote.dto

import java.io.File

data class UserUpdateEmail(
    val name : String?,
    val new_email : String?,
)

data class UserUpdateName(
    val name : String?,
)

data class UserUpdateImage(
    val name : String?,
    val image : File?,
)

data class UserUpdatePassword(
    val name : String?,
    val new_password : String?,
    val confirm_password : String?,
    val old_password : String?,
)
