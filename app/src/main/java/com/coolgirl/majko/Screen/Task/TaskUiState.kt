package com.coolgirl.majko.Screen.Task

import com.coolgirl.majko.R
import com.coolgirl.majko.Screen.TaskEditor.TaskEditorUiState
import com.coolgirl.majko.data.dataUi.Info.InfoUi
import com.coolgirl.majko.data.remote.dto.Info.Info
import com.coolgirl.majko.data.remote.dto.TaskData.TaskDataResponse

data class TaskUiState(
    val allTaskList: List<TaskDataResponse>? = listOf(),
    val favoritesTaskList: List<TaskDataResponse>? = listOf(),
    val searchAllTaskList: List<TaskDataResponse>? = listOf(),
    val searchFavoritesTaskList: List<TaskDataResponse>? = listOf(),
    val searchString: String = DEFAULT_STRING,
    val statuses: List<InfoUi> = listOf(),
    val isError: Boolean = DEFAULT_BOOLEAN,
    val errorMessage: Int? = null,
    val isMessage: Boolean = DEFAULT_BOOLEAN,
    val message: Int? = null,
    val isLongtap: Boolean = DEFAULT_BOOLEAN,
    val longtapTaskId: String = DEFAULT_STRING,
    val expandedFilter: Boolean = DEFAULT_BOOLEAN,
    val expandedLongTap: Boolean = DEFAULT_BOOLEAN,
){
    companion object {
        const val DEFAULT_STRING = ""
        const val DEFAULT_BOOLEAN = false

        fun default() = TaskUiState()
    }
}
