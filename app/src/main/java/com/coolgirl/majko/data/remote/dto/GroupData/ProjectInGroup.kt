package com.coolgirl.majko.data.remote.dto.GroupData

import com.google.gson.annotations.SerializedName

data class ProjectInGroup(

    @SerializedName("project_id") val projectId: String,
    @SerializedName("group_id") val groupId: String

)
