package com.coolgirl.majko.Screen.Archive

import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectDataResponse

data class ArchiveUiState(
    val personalProject : List<ProjectDataResponse>? = listOf(),
    val groupProject : List<ProjectDataResponse>? = listOf(),
    val searchPersonalProject : List<ProjectDataResponse>? = listOf(),
    val searchGroupProject : List<ProjectDataResponse>? = listOf(),
    val searchString: String = "",
    val isLongtap: Boolean = false,
    val longtapProjectId: String = "",
    val isError: Boolean = false,
    val errorMessage: Int? = null,
    val isMessage: Boolean = false,
    val message: Int? = null
)
