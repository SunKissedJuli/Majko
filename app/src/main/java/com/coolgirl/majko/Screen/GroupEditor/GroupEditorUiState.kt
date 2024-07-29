package com.coolgirl.majko.Screen.GroupEditor

import com.coolgirl.majko.data.remote.dto.GroupData.GroupMember
import com.coolgirl.majko.data.remote.dto.GroupData.GroupResponse
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectDataResponse

data class GroupEditorUiState(
    val groupId: String = "",
    val groupData: GroupResponse? = null,
    val projectData: List<ProjectDataResponse>? = listOf(),
    val isAdding: Boolean = false,
    val taskProjectName: String = "",
    val isInvite: Boolean = false,
    val invite: String = "",
    val isMembers: Boolean = false,
    val members: List<GroupMember>? = listOf(),
)
