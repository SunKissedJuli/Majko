package com.coolgirl.majko.Screen.TaskEditor

import com.coolgirl.majko.data.remote.dto.TaskData.TaskDataResponse

data class TaskEditorUiState(
    val taskText: String = "",
    val taskDeadline: String = "",
    val taskPriority: Int = 1,
    val taskStatus: Int = 1,

)
