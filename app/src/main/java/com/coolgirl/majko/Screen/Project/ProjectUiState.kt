package com.coolgirl.majko.Screen.Project

import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectDataResponse

data class ProjectUiState(
    val personalProject : List<ProjectDataResponse>? = null,
    val groupProject : List<ProjectDataResponse>? = null,
    val searchPersonalProject : List<ProjectDataResponse>? = null,
    val searchGroupProject : List<ProjectDataResponse>? = null,
    val searchString: String = "",
    val isAdding: Boolean = false,
    val newProjectName : String = "",
    val newProjectDescription : String = "",
    val isLongtap: Boolean = false,
    val longtapProjectId: String = "",
    val isInvite: Boolean =  false,
    val invite: String = "",
    val invite_message: String = "",
)
