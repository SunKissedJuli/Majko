package com.coolgirl.majko.data.remote.dto.ProjectData

import com.coolgirl.majko.data.remote.dto.CurrentUserDataResponse
import com.coolgirl.majko.data.remote.dto.TaskData.TaskDataResponse

data class ProjectCurrentResponse(
    val id: String,
    val createdAt: String,
    val updatedAt: String,
    val name: String,
    val description: String,
    val is_archive: Int,
    val author: CurrentUserDataResponse,
    val members: List<CurrentUserDataResponse>,
    val image: String?,
    val is_personal: Boolean,
    val count_files: Int,
    val tasks: List<TaskDataResponse>,
    val groups: List<Group>,
    val files: List<File>
)

data class Group(
    val id: String,
    val name: String
)

data class File(
    val id: String,
    val name: String,
    val url: String
)
