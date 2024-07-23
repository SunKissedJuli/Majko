package com.coolgirl.majko.Screen.Archive

import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectDataResponse

data class ArchiveUiState(
    val personalProject : List<ProjectDataResponse>? = null,
    val groupProject : List<ProjectDataResponse>? = null,
    val searchPersonalProject : List<ProjectDataResponse>? = null,
    val searchGroupProject : List<ProjectDataResponse>? = null,
    val searchString: String = "",
    val isLongtap: Boolean = false,
    val longtapProjectId: String = ""
)
