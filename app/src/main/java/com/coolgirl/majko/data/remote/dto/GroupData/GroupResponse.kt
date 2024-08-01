package com.coolgirl.majko.data.remote.dto.GroupData

import com.coolgirl.majko.data.remote.dto.User.CurrentUserDataResponse
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectDataResponse
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectRole
import com.google.gson.annotations.SerializedName

data class GroupResponse(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("author") val author: CurrentUserDataResponse,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("parent_group") val parentGroup: String?,
    @SerializedName("is_personal") val isPersonal: Boolean,
    @SerializedName("image") val image: String?,
    @SerializedName("files_count") val filesCount: Int,
    @SerializedName("projects_group") val projectsGroup: List<ProjectDataResponse>,
    @SerializedName("members") val members: List<GroupMember>,
    @SerializedName("files") val files: List<Any>
)

data class GroupMember(
    @SerializedName("id") val id: String,
    @SerializedName("user") val user: CurrentUserDataResponse,
    @SerializedName("role") val role: ProjectRole
)
