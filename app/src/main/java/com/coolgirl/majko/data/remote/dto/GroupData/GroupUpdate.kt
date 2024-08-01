package com.coolgirl.majko.data.remote.dto.GroupData

import com.google.gson.annotations.SerializedName

data class GroupUpdate(

    @SerializedName("groupId") val groupId: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
)
