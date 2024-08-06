package com.coolgirl.majko.data.dataUi.GroupData

import com.coolgirl.majko.data.remote.dto.GroupData.GroupInviteResponse
import com.google.gson.annotations.SerializedName

data class GroupInviteResponseUi(
    val groupId: String,
    val invite: String,
    val userId: String,
    val id: String,
    val updatedAt: String,
    val createdAt: String
)

fun GroupInviteResponse.toUi(): GroupInviteResponseUi {
    return GroupInviteResponseUi(
        groupId = this.groupId.orEmpty(),
        invite = this.invite.orEmpty(),
        userId = this.userId.orEmpty(),
        id = this.id.orEmpty(),
        updatedAt = this.updatedAt.orEmpty(),
        createdAt = this.createdAt.orEmpty()
    )
}