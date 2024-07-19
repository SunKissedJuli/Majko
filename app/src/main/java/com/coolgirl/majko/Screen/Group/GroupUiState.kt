package com.coolgirl.majko.Screen.Group

import com.coolgirl.majko.data.remote.dto.GroupData.GroupResponse
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectDataResponse

data class GroupUiState(
    val personalGroup : List<GroupResponse>? = null,
    val groupGroup : List<GroupResponse>? = null,
    val searchPersonalGroup : List<GroupResponse>? = null,
    val searchGroupGroup : List<GroupResponse>? = null,
    val searchString: String = "",
    val is_adding: Boolean = false,
    val is_adding_background : Float = 1f,
    val newGroupName : String = "",
    val newGroupDescription : String = "",
)
