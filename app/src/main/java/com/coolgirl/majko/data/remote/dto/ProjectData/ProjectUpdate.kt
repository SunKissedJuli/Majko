package com.coolgirl.majko.data.remote.dto.ProjectData

data class ProjectUpdate(
    val id: String,
    val name: String,
    val description:String,
    val is_archive: Int
)
