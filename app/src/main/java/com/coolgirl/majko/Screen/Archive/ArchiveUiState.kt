package com.coolgirl.majko.Screen.Archive

import com.coolgirl.majko.data.dataUi.ProjectData.ProjectDataResponseUi
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectDataResponse

data class ArchiveUiState(
    val personalProject : List<ProjectDataResponseUi>? = listOf(),
    val groupProject : List<ProjectDataResponseUi>? = listOf(),
    val searchPersonalProject : List<ProjectDataResponseUi>? = listOf(),
    val searchGroupProject : List<ProjectDataResponseUi>? = listOf(),
    val searchString: String = DEFAULT_STRING,
    val isLongtap: Boolean = DEFAULT_BOOLEAN,
    val longtapProjectId: String = DEFAULT_STRING,
    val isError: Boolean = DEFAULT_BOOLEAN,
    val errorMessage: Int? = null,
    val isMessage: Boolean = DEFAULT_BOOLEAN,
    val message: Int? = null,
    val expandedFilter: Boolean = DEFAULT_BOOLEAN
) {
    companion object {
        const val DEFAULT_STRING = ""
        const val DEFAULT_BOOLEAN = false

        fun default() = ArchiveUiState()
    }
}
