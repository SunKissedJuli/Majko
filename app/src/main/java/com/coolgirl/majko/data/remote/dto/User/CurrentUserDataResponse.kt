package com.coolgirl.majko.data.remote.dto.User

import com.google.gson.annotations.SerializedName

data class CurrentUserDataResponse(
    @SerializedName("id") val id : String?,
    @SerializedName("createdAt") val createdAt : String?,
    @SerializedName("updatedAt") val updatedAt : String?,
    @SerializedName("name") val name : String?,
    @SerializedName("image") val image : String?,
    @SerializedName("email") val email : String?,
)
