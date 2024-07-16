package com.coolgirl.majko.Screen.Archive

import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectDataResponse

data class ArchiveUiState(
    val personalProject : List<ProjectDataResponse>? = null,
    val groupProject : List<ProjectDataResponse>? = null,
)
