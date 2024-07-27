package com.coolgirl.majko.data.remote.dto.ProjectData

import com.coolgirl.majko.data.remote.dto.CurrentUserDataResponse
import com.coolgirl.majko.data.remote.dto.TaskData.TaskDataResponse
import com.google.gson.annotations.SerializedName

data class ProjectCurrentResponse(
    val id: String,
    val createdAt: String,
    val updatedAt: String,
    val name: String,
    val description: String,
    @SerializedName("is_archive") var isArchive: Int,
    val author: CurrentUserDataResponse,
    val members: List<Member>,
    val image: String?,
    @SerializedName("is_personal") var isPersonal: Boolean,
    @SerializedName("count_files") val countFiles: Int,
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

data class Member(
    @SerializedName("project_member_id") var projectMemberId: String,
    val user: CurrentUserDataResponse,
    @SerializedName("role_id") var roleId:ProjectRole
)

data class ProjectRole(
    val id: Int,
    val name: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
)
