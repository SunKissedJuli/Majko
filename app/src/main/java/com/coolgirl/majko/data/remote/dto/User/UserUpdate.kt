package com.coolgirl.majko.data.remote.dto

import com.google.gson.annotations.SerializedName
import java.io.File

data class UserUpdateEmail(
    @SerializedName("name") val name : String?,
    @SerializedName("new_email") val newEmail : String?,
)

data class UserUpdateName(
    @SerializedName("name") val name : String?,
)

data class UserUpdateImage(
    @SerializedName("name") val name : String?,
    @SerializedName("image") val image : File?,
)

data class UserUpdatePassword(
    @SerializedName("name") val name : String?,
    @SerializedName("new_password") val newPassword : String?,
    @SerializedName("confirm_password") val confirmPassword : String?,
    @SerializedName("old_password") val oldPassword : String?,
)
