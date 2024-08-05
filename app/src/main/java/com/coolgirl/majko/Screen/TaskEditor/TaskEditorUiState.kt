package com.coolgirl.majko.Screen.TaskEditor

import com.coolgirl.majko.R
import com.coolgirl.majko.data.dataUi.Info.InfoUi
import com.coolgirl.majko.data.remote.dto.Info.Info
import com.coolgirl.majko.data.remote.dto.NoteData.NoteDataResponse
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectDataResponse
import com.coolgirl.majko.data.remote.dto.TaskData.TaskDataResponse

data class TaskEditorUiState(
    val taskText: String = DEFAULT_STRING,
    val taskName: String = DEFAULT_STRING,
    val taskDeadline: String = DEFAULT_STRING,
    val taskPriority: Int = DEFAULT_INT,
    val taskStatus: Int = DEFAULT_INT,
    val taskPriorityName: String = DEFAULT_STRING,
    val taskStatusName: String = "",
    val taskProject: String? = null,
    val taskProjectObj: ProjectDataResponse? = null,
    val taskId: String = DEFAULT_TASK_ID,
    val backgroundColor: Int = DEFAULT_BACKGROUND_COLOR,
    val noteText: String = DEFAULT_STRING,
    val newNote: Boolean = DEFAULT_BOOLEAN,
    val notes: List<NoteDataResponse>? = listOf(),
    val subtask: List<TaskDataResponse>? = listOf(),
    val isAdding: Boolean = DEFAULT_BOOLEAN,
    val subtaskText: String = DEFAULT_STRING,
    val subtaskName: String = DEFAULT_STRING,
    val subtaskDeadline: String = DEFAULT_STRING,
    val subtaskPriority: Int = DEFAULT_INT,
    val subtaskStatus: Int = DEFAULT_INT,
    val subtaskProject: String = DEFAULT_STRING,
    val statuses: List<InfoUi> = listOf(),
    val proprieties: List<InfoUi> = listOf(),
    val exitDialog: Boolean = DEFAULT_BOOLEAN,
    val expanded: Boolean = DEFAULT_BOOLEAN
) {
    companion object {
        const val DEFAULT_STRING = ""
        const val DEFAULT_INT = 1
        const val DEFAULT_TASK_ID = "0"
        const val DEFAULT_BACKGROUND_COLOR = R.color.white
        const val DEFAULT_BOOLEAN = false

        fun default() = TaskEditorUiState()
    }
}
