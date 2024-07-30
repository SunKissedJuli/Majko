package com.coolgirl.majko.Screen.Project

import com.coolgirl.majko.R
import com.coolgirl.majko.data.remote.dto.CurrentUserDataResponse
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectDataResponse

data class ProjectUiState(
    val personalProject : List<ProjectDataResponse>? = listOf(),
    val groupProject : List<ProjectDataResponse>? = listOf(),
    val searchPersonalProject : List<ProjectDataResponse>? = listOf(),
    val searchGroupProject : List<ProjectDataResponse>? = listOf(),
    val searchString: String = "",
    val isAdding: Boolean = false,
    val newProjectName : String = "",
    val newProjectDescription : String = "",
    val isLongtap: Boolean = false,
    val longtapProjectId: String = "",
    val isInvite: Boolean =  false,
    val invite: String = "",
    val invite_message: String = "",
    val isError: Boolean = false,
    val errorMessage: Int? = null,
    val isMessage: Boolean = false,
    val message: Int? = null
)
