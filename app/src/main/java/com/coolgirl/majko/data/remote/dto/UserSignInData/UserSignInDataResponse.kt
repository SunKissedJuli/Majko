package com.coolgirl.majko.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserSignInDataResponse (
    @SerializedName("message") var message: String?,
    @SerializedName("status") var status: Int?,
    @SerializedName("accessToken") var accessToken: String?,
)