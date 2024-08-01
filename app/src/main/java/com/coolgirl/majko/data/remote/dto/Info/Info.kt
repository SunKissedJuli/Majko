package com.coolgirl.majko.data.remote.dto.Info

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName

data class Info(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,

    )
