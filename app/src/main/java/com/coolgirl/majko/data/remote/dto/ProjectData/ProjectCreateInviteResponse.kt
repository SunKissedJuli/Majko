package com.coolgirl.majko.data.remote.dto.ProjectData

import com.google.gson.annotations.SerializedName

data class ProjectCreateInviteResponse(
    @SerializedName("project_id") val projectId: String?,
    @SerializedName("invite") val invite: String?,
    @SerializedName("user_id") val userId: String?,
    @SerializedName("id") val id: String?,
    @SerializedName("updated_at") val updatedAt: String?,
    @SerializedName("created_at") val createdAt: String?,
)
