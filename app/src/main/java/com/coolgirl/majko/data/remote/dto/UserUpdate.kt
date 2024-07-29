package com.coolgirl.majko.data.remote.dto

import com.google.gson.annotations.SerializedName
import java.io.File

data class UserUpdateEmail(
    val name : String?,
    @SerializedName("new_email") val newEmail : String?,
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
    @SerializedName("new_password") val newPassword : String?,
    @SerializedName("confirm_password") val confirmPassword : String?,
    @SerializedName("old_password") val oldPassword : String?,
)
