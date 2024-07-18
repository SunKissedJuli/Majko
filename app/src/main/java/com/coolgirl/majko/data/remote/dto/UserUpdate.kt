package com.coolgirl.majko.data.remote.dto

data class UserUpdateEmail(
    val name : String?,
    val new_email : String?,
)

data class UserUpdateName(
    val name : String?,
)

data class UserUpdatePassword(
    val name : String?,
    val new_password : String?,
    val confirm_password : String?,
    val old_password : String?,
)
