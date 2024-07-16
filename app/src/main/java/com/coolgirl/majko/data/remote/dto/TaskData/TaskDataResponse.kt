package com.coolgirl.majko.data.remote.dto.TaskData

import com.coolgirl.majko.data.remote.dto.CurrentUserDataResponse
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectDataResponse

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
    val task_members: List<CurrentUserDataResponse>?,
    val is_personal: Boolean,
    val count_subtasks: Int,
    val count_notes: Int,
    val count_files: Int,
    val is_favorite: Boolean,
    val project: ProjectDataResponse?
)

