package com.coolgirl.majko.Screen.Task

import com.coolgirl.majko.R
import com.coolgirl.majko.Screen.TaskEditor.TaskEditorUiState
import com.coolgirl.majko.data.dataUi.Info.InfoUi
import com.coolgirl.majko.data.dataUi.TaskData.TaskDataResponseUi
import com.coolgirl.majko.data.remote.dto.Info.Info
import com.coolgirl.majko.data.remote.dto.TaskData.TaskDataResponse

data class TaskUiState(
    val allTaskList: List<TaskDataResponseUi>? = listOf(),
    val favoritesTaskList: List<TaskDataResponseUi>? = listOf(),
    val searchAllTaskList: List<TaskDataResponseUi>? = listOf(),
    val searchFavoritesTaskList: List<TaskDataResponseUi>? = listOf(),
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
