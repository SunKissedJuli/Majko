package com.coolgirl.majko.Screen.ProjectEdit

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
    val is_adding: Boolean = false,
    val taskProjectName: String = "",
    val isInvite: Boolean = false,
    val isInviteBackground: Float = 1f,
    val invite: String = "",
    val members: List<Member>? = null
)
