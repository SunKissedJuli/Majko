package com.coolgirl.majko.data.remote.dto.GroupData

import com.coolgirl.majko.data.remote.dto.CurrentUserDataResponse
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectDataResponse

data class GroupResponse(
    val id: String,
    val title: String,
    val description: String,
    val author: CurrentUserDataResponse,
    val created_at: String,
    val updated_at: String,
    val parent_group: String?,
    val is_personal: Boolean,
    val image: String?,
    val files_count: Int,
    val projects_group: List<ProjectDataResponse>,
    val members: List<CurrentUserDataResponse>,
    val files: List<Any>
)
