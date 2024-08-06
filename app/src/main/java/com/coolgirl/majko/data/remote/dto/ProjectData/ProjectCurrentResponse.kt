package com.coolgirl.majko.data.remote.dto.ProjectData

import com.coolgirl.majko.data.remote.dto.User.CurrentUserDataResponse
import com.coolgirl.majko.data.remote.dto.TaskData.TaskDataResponse
import com.google.gson.annotations.SerializedName

data class ProjectCurrentResponse(
    @SerializedName("id") val id: String?,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("updatedAt") val updatedAt: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("is_archive") var isArchive: Int?,
    @SerializedName("author") val author: CurrentUserDataResponse?,
    @SerializedName("members") val members: List<Member>?,
    @SerializedName("image") val image: String?,
    @SerializedName("is_personal") var isPersonal: Boolean?,
    @SerializedName("count_files") val countFiles: Int?,
    @SerializedName("tasks") val tasks: List<TaskDataResponse>?,
    @SerializedName("groups") val groups: List<Group>?,
    @SerializedName("files") val files: List<File>?
)

data class Group(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?
)

data class File(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("url") val url: String?
)

data class Member(
    @SerializedName("project_member_id") var projectMemberId: String?,
    @SerializedName("user") val user: CurrentUserDataResponse?,
    @SerializedName("role_id") var roleId:ProjectRole?
)

data class ProjectRole(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?,
)
