package com.coolgirl.majko.data.remote.dto.TaskData

import com.coolgirl.majko.data.remote.dto.User.CurrentUserDataResponse
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectDataResponse
import com.google.gson.annotations.SerializedName

data class TaskDataResponse(
    @SerializedName("id") val id: String?,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("updatedAt") val updatedAt: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("text") val text: String?,
    @SerializedName("priority") val priority: Int?,
    @SerializedName("deadline") val deadline: String?,
    @SerializedName("status") val status: Int?,
    @SerializedName("image") val image: String?,
    @SerializedName("creator") val creator: List<CurrentUserDataResponse>?,
    @SerializedName("mainTaskId") val mainTaskId: String?,
    @SerializedName("task_members") val taskMembers: List<CurrentUserDataResponse>?,
    @SerializedName("is_personal") val isPersonal: Boolean?,
    @SerializedName("count_subtasks") val countSubtasks: Int?,
    @SerializedName("count_notes") val countNotes: Int?,
    @SerializedName("count_files") val countFiles: Int?,
    @SerializedName("is_favorite") val isFavorite: Boolean?,
    @SerializedName("project") val project: ProjectDataResponse?
)

