package com.coolgirl.majko.data.dataUi.ProjectData

import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectCreateInviteResponse

data class ProjectCreateInviteResponseUi(
    val projectId: String,
    val invite: String,
    val userId: String,
    val id: String,
    val updatedAt: String,
    val createdAt: String
)

fun ProjectCreateInviteResponse.toUi(): ProjectCreateInviteResponseUi {
    return ProjectCreateInviteResponseUi(
        projectId = this.projectId.orEmpty(),
        invite = this.invite.orEmpty(),
        userId = this.userId.orEmpty(),
        id = this.id.orEmpty(),
        updatedAt = this.updatedAt.orEmpty(),
        createdAt = this.createdAt.orEmpty()
    )
}
