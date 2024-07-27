package com.coolgirl.majko.data.remote.dto.Info

import kotlinx.serialization.SerialName

data class Info(
    val id: Int,
    val name: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,

    )
