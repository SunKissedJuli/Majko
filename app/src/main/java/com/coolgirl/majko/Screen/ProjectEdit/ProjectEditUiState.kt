package com.coolgirl.majko.Screen.ProjectEdit

import com.coolgirl.majko.R
import com.coolgirl.majko.Screen.TaskEditor.TaskEditorUiState
import com.coolgirl.majko.data.dataUi.Info.InfoUi
import com.coolgirl.majko.data.dataUi.ProjectData.MemberUi
import com.coolgirl.majko.data.dataUi.ProjectData.ProjectCurrentResponseUi
import com.coolgirl.majko.data.remote.dto.Info.Info
import com.coolgirl.majko.data.remote.dto.ProjectData.Member
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectCurrentResponse

data class ProjectEditUiState(
    val projectId: String = DEFAULT_STRING,
    val projectData: ProjectCurrentResponseUi = ProjectCurrentResponseUi.empty(),
    val taskText: String = DEFAULT_STRING,
    val taskName: String = DEFAULT_STRING,
    val taskDeadline: String = DEFAULT_STRING,
    val taskPriority: Int = DEFAULT_INT,
    val taskStatus: Int = DEFAULT_INT,
    val taskProject: String = DEFAULT_STRING,
    val isAdding: Boolean = DEFAULT_BOOLEAN,
    val taskProjectName: String = DEFAULT_STRING,
    val isInvite: Boolean = DEFAULT_BOOLEAN,
    val invite: String = DEFAULT_STRING,
    val members: List<MemberUi>? = listOf(),
    val isMembers: Boolean = DEFAULT_BOOLEAN,
    val statuses: List<InfoUi> = listOf(),
    val proprieties: List<InfoUi> = listOf(),
    val exitDialog: Boolean = DEFAULT_BOOLEAN,
    val expanded: Boolean = DEFAULT_BOOLEAN,
) {
    companion object {
        const val DEFAULT_STRING = ""
        const val DEFAULT_INT = 1
        const val DEFAULT_BOOLEAN = false

        fun default() = ProjectEditUiState()
    }
}
