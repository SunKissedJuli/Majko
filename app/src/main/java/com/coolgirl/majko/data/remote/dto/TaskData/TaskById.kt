package com.coolgirl.majko.data.remote.dto.TaskData

import com.google.gson.annotations.SerializedName

data class TaskById(
    val taskId: String,
)

data class TaskBy_Id(
    @SerializedName("task_id") val taskId: String,
)
