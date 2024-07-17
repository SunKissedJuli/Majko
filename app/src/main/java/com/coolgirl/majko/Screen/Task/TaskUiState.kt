package com.coolgirl.majko.Screen.Task

import com.coolgirl.majko.data.remote.dto.TaskData.TaskDataResponse

data class TaskUiState(
    val allTaskList: List<TaskDataResponse>? = null,
    val favoritesTaskList: List<TaskDataResponse>? = null,
    val searchAllTaskList: List<TaskDataResponse>? = null,
    val searchFavoritesTaskList: List<TaskDataResponse>? = null,
    val searchString: String = ""
)
