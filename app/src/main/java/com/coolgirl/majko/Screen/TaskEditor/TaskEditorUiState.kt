package com.coolgirl.majko.Screen.TaskEditor

import com.coolgirl.majko.R

data class TaskEditorUiState(
    val taskText: String = "",
    val taskName: String = "",
    val taskDeadline: String = "",
    val taskPriority: Int = 1,
    val taskStatus: Int = 1,
    val taskProject: String = "",
    val taskId: String = "0",
    val backgroundColor: Int = R.color.white
)
