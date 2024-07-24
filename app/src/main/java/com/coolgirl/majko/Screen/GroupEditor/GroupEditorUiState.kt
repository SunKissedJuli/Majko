package com.coolgirl.majko.Screen.GroupEditor

import com.coolgirl.majko.data.remote.dto.GroupData.GroupResponse
import com.coolgirl.majko.data.remote.dto.ProjectData.Member
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectDataResponse

data class GroupEditorUiState(
    val group_id: String = "",
    val groupData: GroupResponse? = null,
    val projectData: List<ProjectDataResponse>? = null,
    val is_adding: Boolean = false,
    val taskProjectName: String = "",
    val is_invite: Boolean = false,
    val invite: String = "",
    val members: List<Member>? = null
)
