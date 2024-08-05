package com.coolgirl.majko.Screen.ProjectEdit

import com.coolgirl.majko.data.dataUi.Info.InfoUi
import com.coolgirl.majko.data.remote.dto.Info.Info
import com.coolgirl.majko.data.remote.dto.ProjectData.Member
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectCurrentResponse

data class ProjectEditUiState(
    val projectId: String = "",
    val projectData: ProjectCurrentResponse? = null,
    val taskText: String = "",
    val taskName: String = "",
    val taskDeadline: String = "",
    val taskPriority: Int = 1,
    val taskStatus: Int = 1,
    val taskProject: String = "",
    val isAdding: Boolean = false,
    val taskProjectName: String = "",
    val isInvite: Boolean = false,
    val invite: String = "",
    val members: List<Member>? = listOf(),
    val isMembers: Boolean = false,
    val statuses: List<InfoUi> = listOf(),
    val proprieties: List<InfoUi> = listOf(),
    val exitDialog: Boolean = false,
    val expanded: Boolean = false,
)
