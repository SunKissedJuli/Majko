package com.coolgirl.majko.domain.repository

import com.coolgirl.majko.data.remote.ApiResult
import com.coolgirl.majko.data.remote.dto.MessageData
import com.coolgirl.majko.data.remote.dto.ProjectData.*
import com.coolgirl.majko.data.remote.dto.TaskData.SearchTask
import kotlinx.coroutines.flow.Flow

interface MajkoProjectRepositoryInterface {

    fun getPersonalProject(search: SearchTask): Flow<ApiResult<List<ProjectDataResponse>>>

    fun getGroupProject(search: SearchTask): Flow<ApiResult<List<ProjectDataResponse>>>

    fun postNewProject(project: ProjectData): Flow<ApiResult<ProjectDataResponse>>

    fun getProjectById(projectById: ProjectById): Flow<ApiResult<ProjectCurrentResponse>>

    fun updateProject(project: ProjectUpdate): Flow<ApiResult<ProjectCurrentResponse>>

    fun removeProject(projectId: ProjectById): Flow<ApiResult<Unit>>

    fun createInvitetoProject(projectById: ProjectByIdUnderscore): Flow<ApiResult<ProjectCreateInviteResponse>>

    fun joinByInvitation(invite: JoinByInviteProjectData): Flow<ApiResult<MessageData>>
}