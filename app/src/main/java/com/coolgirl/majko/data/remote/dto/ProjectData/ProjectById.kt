package com.coolgirl.majko.data.remote.dto.ProjectData

import com.google.gson.annotations.SerializedName

data class ProjectById(
    @SerializedName("projectId") val projectId : String,
)

data class ProjectByIdUnderscore (
    @SerializedName("project_id") val projectId : String,
)
