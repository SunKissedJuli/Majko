package com.coolgirl.majko.data.remote.dto.TaskData

import com.google.gson.annotations.SerializedName

data class TaskUpdateData(
    @SerializedName("taskId") val taskId : String,
    @SerializedName("title") val title : String,
    @SerializedName("text") val text : String,
    @SerializedName("priority_id") val priorityId : Int,
    @SerializedName("deadline") val deadline : String,
    @SerializedName("status_id") val statusId : Int,
)
