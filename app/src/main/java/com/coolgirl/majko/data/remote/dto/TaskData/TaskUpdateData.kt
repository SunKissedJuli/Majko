package com.coolgirl.majko.data.remote.dto.TaskData

import com.google.gson.annotations.SerializedName

data class TaskUpdateData(
    val taskId : String,
    val title : String,
    val text : String,
    @SerializedName("priority_id") val priorityId : Int,
    val deadline : String,
    @SerializedName("status_id") val statusId : Int,
)
