package com.coolgirl.majko.data.remote.dto.UserSignUpData

import com.google.gson.annotations.SerializedName

data class UserSignUpDataResponse(
    @SerializedName("accessToken") val accessToken: String?,
    @SerializedName("refreshToken") val refreshToken: String?
)
