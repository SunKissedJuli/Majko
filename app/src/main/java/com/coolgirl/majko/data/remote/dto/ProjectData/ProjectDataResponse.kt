package com.coolgirl.majko.data.remote.dto.ProjectData

import com.coolgirl.majko.data.remote.dto.CurrentUserDataResponse
import com.google.gson.annotations.SerializedName

data class ProjectDataResponse(
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
    val countFiles: Int
    )

