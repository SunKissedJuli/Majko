package com.coolgirl.majko.Screen.Task

import com.coolgirl.majko.data.remote.dto.Info.Info
import com.coolgirl.majko.data.remote.dto.TaskData.TaskDataResponse

data class TaskUiState(
    val allTaskList: List<TaskDataResponse>? = listOf(),
    val favoritesTaskList: List<TaskDataResponse>? = listOf(),
    val searchAllTaskList: List<TaskDataResponse>? = listOf(),
    val searchFavoritesTaskList: List<TaskDataResponse>? = listOf(),
    val searchString: String = "",
    val statuses: List<Info>? = null,
    val isError: Boolean = false,
    val errorMessage: Int? = null,
    val isMessage: Boolean = false,
    val message: Int? = null,
    val isLongtap: Boolean = false,
    val longtapTaskId: String = "",
)
