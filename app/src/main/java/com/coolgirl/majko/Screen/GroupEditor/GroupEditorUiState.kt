package com.coolgirl.majko.Screen.GroupEditor

import com.coolgirl.majko.Screen.Login.LoginUiState
import com.coolgirl.majko.data.remote.dto.GroupData.GroupMember
import com.coolgirl.majko.data.remote.dto.GroupData.GroupResponse
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectDataResponse

data class GroupEditorUiState(
    val groupId: String = DEFAULT_STRING,
    val groupData: GroupResponse? = null,
    val projectData: List<ProjectDataResponse>? = listOf(),
    val isAdding: Boolean = DEFAULT_BOOLEAN,
    val taskProjectName: String = DEFAULT_STRING,
    val isInvite: Boolean = DEFAULT_BOOLEAN,
    val invite: String = DEFAULT_STRING,
    val isMembers: Boolean = DEFAULT_BOOLEAN,
    val members: List<GroupMember>? = listOf(),
    val exitDialog: Boolean = DEFAULT_BOOLEAN,
    val expanded: Boolean = DEFAULT_BOOLEAN,
){
    companion object {
        const val DEFAULT_STRING = ""
        const val DEFAULT_BOOLEAN = false

        fun default() = GroupEditorUiState()
    }
}

