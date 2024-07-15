package com.coolgirl.majko.data.remote.dto.TaskData

import com.coolgirl.majko.data.remote.dto.CurrentUserDataResponse
import com.coolgirl.majko.data.remote.dto.ProjectDataResponse

data class TaskDataResponse(
    val id: String,
    val createdAt: String,
    val updatedAt: String,
    val title: String?,
    val text: String?,
    val priority: Int,
    val deadline: String,
    val status: Int,
    val image: String?,
    val creator: List<CurrentUserDataResponse>,
    val mainTaskId: String?,
    val taskMembers: List<String>?,
    val isPersonal: Boolean,
    val countSubtasks: Int,
    val countNotes: Int,
    val countFiles: Int,
    val isFavorite: Boolean,
    val project: ProjectDataResponse?
)

