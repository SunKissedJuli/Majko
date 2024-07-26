package com.coolgirl.majko.Screen.TaskEditor

import com.coolgirl.majko.R
import com.coolgirl.majko.data.remote.dto.Info.Info
import com.coolgirl.majko.data.remote.dto.NoteData.NoteDataResponse
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectDataResponse
import com.coolgirl.majko.data.remote.dto.TaskData.TaskDataResponse

data class TaskEditorUiState(
    val taskText: String = "",
    val taskName: String = "",
    val taskDeadline: String = "",
    val taskPriority: Int = 1,
    val taskStatus: Int = 1,
    val taskPriorityName: String = "",
    val taskStatusName: String = "",
    val taskProject: String = "",
    val taskProjectObj: ProjectDataResponse? = null,
    val taskId: String = "0",
    val backgroundColor: Int = R.color.white,
    val noteText: String = "",
    val newNote:Boolean = false,
    val notes: List<NoteDataResponse>? = listOf(),
    val subtask: List<TaskDataResponse>? = listOf(),
    val isAdding: Boolean = false,
    val subtaskText: String = "",
    val subtaskName: String = "",
    val subtaskDeadline: String = "",
    val subtaskPriority: Int = 1,
    val subtaskStatus: Int = 1,
    val subtaskProject: String = "",
    val statuses: List<Info>? = listOf(),
    val proprieties: List<Info>? = listOf(),
)
