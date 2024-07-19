package com.coolgirl.majko.data.remote.dto.GroupData

data class GroupInviteResponse(
    val group_id: String,
    val invite: String,
    val user_id: String,
    val id: String,
    val updated_at: String,
    val created_at: String
)
