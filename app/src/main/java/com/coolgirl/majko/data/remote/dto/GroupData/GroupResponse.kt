package com.coolgirl.majko.data.remote.dto.GroupData

import com.coolgirl.majko.data.remote.dto.CurrentUserDataResponse
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectDataResponse
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectRole
import com.google.gson.annotations.SerializedName

data class GroupResponse(
    val id: String,
    val title: String,
    val description: String,
    val author: CurrentUserDataResponse,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("parent_group") val parentGroup: String?,
    @SerializedName("is_personal") val isPersonal: Boolean,
    val image: String?,
    @SerializedName("files_count") val filesCount: Int,
    @SerializedName("projects_group") val projectsGroup: List<ProjectDataResponse>,
    val members: List<GroupMember>,
    val files: List<Any>
)

data class GroupMember(
    val id: String,
    val user: CurrentUserDataResponse,
    val role: ProjectRole
)
