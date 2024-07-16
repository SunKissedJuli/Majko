package com.coolgirl.majko.data.remote.dto.ProjectData

import com.coolgirl.majko.data.remote.dto.CurrentUserDataResponse

data class ProjectDataResponse(
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
    val countFiles: Int
    )
