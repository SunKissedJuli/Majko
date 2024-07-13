package com.coolgirl.majko.data.remote.dto

data class ProjectDataResponse(
    val id: String,
    val createdAt: String,
    val updatedAt: String,
    val name: String,
    val description: String,
    val isArchive: Int,
    val author: CurrentUserDataResponse,
    val members: List<String>,
    val image: String?,
    val isPersonal: Boolean,
    val countFiles: Int
    )
