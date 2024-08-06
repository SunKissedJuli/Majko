package com.coolgirl.majko.Screen.Project

import com.coolgirl.majko.R
import com.coolgirl.majko.Screen.TaskEditor.TaskEditorUiState
import com.coolgirl.majko.data.dataUi.ProjectData.ProjectDataResponseUi
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectDataResponse

data class ProjectUiState(
    val personalProject : List<ProjectDataResponseUi>? = listOf(),
    val groupProject : List<ProjectDataResponseUi>? = listOf(),
    val searchPersonalProject : List<ProjectDataResponseUi>? = listOf(),
    val searchGroupProject : List<ProjectDataResponseUi>? = listOf(),
    val searchString: String = DEFAULT_STRING,
    val isAdding: Boolean = DEFAULT_BOOLEAN,
    val newProjectName : String = DEFAULT_STRING,
    val newProjectDescription : String = DEFAULT_STRING,
    val isLongtap: Boolean = DEFAULT_BOOLEAN,
    val longtapProjectId: String = DEFAULT_STRING,
    val isInvite: Boolean =  DEFAULT_BOOLEAN,
    val invite: String = DEFAULT_STRING,
    val invite_message: String = DEFAULT_STRING,
    val isError: Boolean = DEFAULT_BOOLEAN,
    val errorMessage: Int? = null,
    val isMessage: Boolean = DEFAULT_BOOLEAN,
    val message: Int? = null,
    val expandedFilter: Boolean = DEFAULT_BOOLEAN,
    val expanded: Boolean = DEFAULT_BOOLEAN,
    val expandedLongTap: Boolean = DEFAULT_BOOLEAN
){
    companion object {
        const val DEFAULT_STRING = ""
        const val DEFAULT_BOOLEAN = false

        fun default() = ProjectUiState()
    }
}
