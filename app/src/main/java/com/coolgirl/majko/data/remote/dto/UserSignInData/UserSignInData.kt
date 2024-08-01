package com.coolgirl.majko.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserSignInData(
    @SerializedName("email") var email: String?,
    @SerializedName("password") var password: String?,
)
