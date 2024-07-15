package com.coolgirl.majko.data.remote.dto.TaskData

data class TaskData(
    val title : String?,
    val text : String?,
    val deadline : String?,
    val priority_id : Int?,
    val status_id : Int?,
    val project_id : String?,
    val mainTaskId : String?,
)
