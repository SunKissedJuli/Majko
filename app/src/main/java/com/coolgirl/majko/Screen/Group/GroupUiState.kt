package com.coolgirl.majko.Screen.Group

import com.coolgirl.majko.R
import com.coolgirl.majko.Screen.TaskEditor.TaskEditorUiState
import com.coolgirl.majko.data.remote.dto.GroupData.GroupResponse
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectDataResponse

data class GroupUiState(
    val personalGroup : List<GroupResponse>? = listOf(),
    val groupGroup : List<GroupResponse>? = listOf(),
    val searchPersonalGroup : List<GroupResponse>? = listOf(),
    val searchGroupGroup : List<GroupResponse>? = listOf(),
    val searchString: String = DEFAULT_STRING,
    val isAdding: Boolean = DEFAULT_BOOLEAN,
    val newGroupName : String = DEFAULT_STRING,
    val newGroupDescription : String = DEFAULT_STRING,
    val isInvite: Boolean =  DEFAULT_BOOLEAN,
    val invite: String = DEFAULT_STRING,
    val inviteMessage: String = DEFAULT_STRING,
    val isError: Boolean = DEFAULT_BOOLEAN,
    val errorMessage: Int? = null,
    val isMessage: Boolean = DEFAULT_BOOLEAN,
    val message: Int? = null,
    val isLongtap: Boolean = DEFAULT_BOOLEAN,
    val longtapGroupId: String = DEFAULT_STRING,
    val expandedFilter: Boolean = DEFAULT_BOOLEAN,
    val expanded: Boolean = DEFAULT_BOOLEAN,
    val expandedLongTap: Boolean = DEFAULT_BOOLEAN,
) {
    companion object {
        const val DEFAULT_STRING = ""
        const val DEFAULT_BOOLEAN = false

        fun default() = GroupUiState()
    }
}
