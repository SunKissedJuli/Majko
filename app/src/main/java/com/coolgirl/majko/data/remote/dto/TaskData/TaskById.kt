package com.coolgirl.majko.data.remote.dto.TaskData

import com.google.gson.annotations.SerializedName

data class TaskById(
    @SerializedName("taskId") val taskId: String,
)

data class TaskByIdUnderscore(
    @SerializedName("task_id") val taskId: String,
)
