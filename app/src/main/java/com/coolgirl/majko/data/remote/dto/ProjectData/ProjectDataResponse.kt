package com.coolgirl.majko.data.remote.dto.ProjectData

import com.coolgirl.majko.data.remote.dto.User.CurrentUserDataResponse
import com.google.gson.annotations.SerializedName

data class ProjectDataResponse(
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
    @SerializedName("countFiles") val countFiles: Int?,
)

