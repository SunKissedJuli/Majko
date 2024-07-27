package com.coolgirl.majko.data.remote.dto.TaskData

import com.coolgirl.majko.data.remote.dto.CurrentUserDataResponse
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectDataResponse
import com.google.gson.annotations.SerializedName

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
    @SerializedName("task_members") val taskMembers: List<CurrentUserDataResponse>?,
    @SerializedName("is_personal") val isPersonal: Boolean,
    @SerializedName("count_subtasks") val countSubtasks: Int,
    @SerializedName("count_notes") val countNotes: Int,
    @SerializedName("count_files") val countFiles: Int,
    @SerializedName("is_favorite") val isFavorite: Boolean,
    val project: ProjectDataResponse?
)

