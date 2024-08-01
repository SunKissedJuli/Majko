package com.coolgirl.majko.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MessageData(
    @SerializedName("message") val message : String?
)
