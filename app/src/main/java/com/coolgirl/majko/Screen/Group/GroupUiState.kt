package com.coolgirl.majko.Screen.Group

import com.coolgirl.majko.data.remote.dto.GroupData.GroupResponse
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectDataResponse

data class GroupUiState(
    val personalGroup : List<GroupResponse>? = null,
    val groupGroup : List<GroupResponse>? = null,
    val searchPersonalGroup : List<GroupResponse>? = null,
    val searchGroupGroup : List<GroupResponse>? = null,
    val searchString: String = "",
    val isAdding: Boolean = false,
    val isAddingBackground : Float = 1f,
    val newGroupName : String = "",
    val newGroupDescription : String = "",
    val isInvite: Boolean =  false,
    val invite: String = "",
    val invite_message: String = "",
)
