package com.coolgirl.majko.Screen.TaskEditor

import com.coolgirl.majko.R
import com.coolgirl.majko.data.remote.dto.NoteData.NoteDataResponse
import com.coolgirl.majko.data.remote.dto.TaskData.TaskDataResponse

data class TaskEditorUiState(
    val taskText: String = "",
    val taskName: String = "",
    val taskDeadline: String = "",
    val taskPriority: Int = 1,
    val taskStatus: Int = 1,
    val taskProject: String = "",
    val taskId: String = "0",
    val backgroundColor: Int = R.color.white,
    val noteText: String = "",
    val newNote:Boolean = false,
    val notes: List<NoteDataResponse>? = null,
    val subtask: List<TaskDataResponse>? = null
)
