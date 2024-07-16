package com.coolgirl.majko.Screen.Project

import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectDataResponse

data class ProjectUiState(
    val personalProject : List<ProjectDataResponse>? = null,
    val groupProject : List<ProjectDataResponse>? = null,
    val is_adding: Boolean = false,
    val is_adding_background : Float = 1f,
    val newProjectName : String = "",
    val newProjectDescription : String = "",
)
