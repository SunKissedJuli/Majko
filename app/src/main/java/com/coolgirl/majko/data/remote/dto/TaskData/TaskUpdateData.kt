package com.coolgirl.majko.data.remote.dto.TaskData

data class TaskUpdateData(
    val taskId : String,
    val title : String,
    val text : String,
    val priority_id : Int,
    val deadline : String,
    val status_id : Int,
)
