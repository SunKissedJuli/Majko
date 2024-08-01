package com.coolgirl.majko.data.remote.dto.UserSignUpData

import com.google.gson.annotations.SerializedName

data class UserSignUpData(
    @SerializedName("email") var email: String?,
    @SerializedName("password") var password: String?,
    @SerializedName("name") var name: String?
)
