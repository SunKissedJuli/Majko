package com.coolgirl.majko.data.remote.dto.ProjectData

data class ProjectCreateInviteResponse(
    val project_id: String,
    val invite: String,
    val user_id: String,
    val id: String,
    val updated_at: String,
    val created_at: String,
)
