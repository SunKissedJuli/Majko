package com.coolgirl.majko.Screen.Group

import com.coolgirl.majko.data.remote.dto.GroupData.GroupResponse
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectDataResponse

data class GroupUiState(
    val personalGroup : List<GroupResponse>? = listOf(),
    val groupGroup : List<GroupResponse>? = listOf(),
    val searchPersonalGroup : List<GroupResponse>? = listOf(),
    val searchGroupGroup : List<GroupResponse>? = listOf(),
    val searchString: String = "",
    val isAdding: Boolean = false,
    val newGroupName : String = "",
    val newGroupDescription : String = "",
    val isInvite: Boolean =  false,
    val invite: String = "",
    val inviteMessage: String = "",
    val isError: Boolean = false,
    val errorMessage: Int? = null,
    val isMessage: Boolean = false,
    val message: Int? = null,
    val isLongtap: Boolean = false,
    val longtapGroupId: String = "",
)
