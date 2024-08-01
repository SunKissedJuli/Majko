package com.coolgirl.majko.data.remote.dto.User

import com.google.gson.annotations.SerializedName

data class CurrentUserDataResponse(
    @SerializedName("id") var id : String,
    @SerializedName("createdAt") var createdAt : String,
    @SerializedName("updatedAt") var updatedAt : String,
    @SerializedName("name") var name : String?,
    @SerializedName("image") var image : String?,
    @SerializedName("email") var email : String,
)
