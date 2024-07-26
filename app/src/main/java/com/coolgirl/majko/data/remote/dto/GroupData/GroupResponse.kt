package com.coolgirl.majko.data.remote.dto.GroupData

import com.coolgirl.majko.data.remote.dto.CurrentUserDataResponse
import com.coolgirl.majko.data.remote.dto.ProjectData.Member
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectDataResponse
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectRole

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
    val members: List<GroupMember>,
    val files: List<Any>
)

data class GroupMember(
    val id: String,
    val user: CurrentUserDataResponse,
    val role: ProjectRole
)
