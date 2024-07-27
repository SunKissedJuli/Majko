package com.coolgirl.majko.data.remote.dto.TaskData

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName

data class TaskData(
    val title : String?,
    val text : String?,
    val deadline : String?,
    @SerializedName("priority_id") val priorityId : Int?,
    @SerializedName("status_id") val statusId : Int?,
    @SerializedName("project_id") val projectId : String?,
    val mainTaskId : String?,
)
