package com.coolgirl.majko.Screen.GroupEditor

import com.coolgirl.majko.data.remote.dto.GroupData.GroupResponse
import com.coolgirl.majko.data.remote.dto.ProjectData.Member
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectDataResponse

data class GroupEditorUiState(
    val groupId: String = "",
    val groupData: GroupResponse? = null,
    val projectData: List<ProjectDataResponse>? = null,
    val isAdding: Boolean = false,
    val taskProjectName: String = "",
    val isInvite: Boolean = false,
    val invite: String = "",
    val members: List<Member>? = null
)
