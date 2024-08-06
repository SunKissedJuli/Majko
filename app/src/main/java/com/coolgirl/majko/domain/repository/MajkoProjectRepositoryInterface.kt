package com.coolgirl.majko.domain.repository

import com.coolgirl.majko.data.dataUi.MessageDataUi
import com.coolgirl.majko.data.dataUi.ProjectData.ProjectCreateInviteResponseUi
import com.coolgirl.majko.data.dataUi.ProjectData.ProjectCurrentResponseUi
import com.coolgirl.majko.data.dataUi.ProjectData.ProjectDataResponseUi
import com.coolgirl.majko.data.remote.ApiResult
import com.coolgirl.majko.data.remote.dto.MessageData
import com.coolgirl.majko.data.remote.dto.ProjectData.*
import com.coolgirl.majko.data.remote.dto.TaskData.SearchTask
import kotlinx.coroutines.flow.Flow

interface MajkoProjectRepositoryInterface {

    fun getPersonalProject(search: SearchTask): Flow<ApiResult<List<ProjectDataResponseUi>>>

    fun getGroupProject(search: SearchTask): Flow<ApiResult<List<ProjectDataResponseUi>>>

    fun postNewProject(project: ProjectData): Flow<ApiResult<ProjectDataResponseUi>>

    fun getProjectById(projectById: ProjectById): Flow<ApiResult<ProjectCurrentResponseUi>>

    fun updateProject(project: ProjectUpdate): Flow<ApiResult<ProjectCurrentResponseUi>>

    fun removeProject(projectId: ProjectById): Flow<ApiResult<Unit>>

    fun createInvitetoProject(projectById: ProjectByIdUnderscore): Flow<ApiResult<ProjectCreateInviteResponseUi>>

    fun joinByInvitation(invite: JoinByInviteProjectData): Flow<ApiResult<MessageDataUi>>
}