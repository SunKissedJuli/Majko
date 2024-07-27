package com.coolgirl.majko.data.remote.dto.ProjectData

import com.google.gson.annotations.SerializedName

data class ProjectById(
    val projectId : String,
)

data class ProjectBy_Id(
    @SerializedName("project_id") val projectId : String,
)
