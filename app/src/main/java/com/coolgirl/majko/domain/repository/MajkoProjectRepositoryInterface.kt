package com.coolgirl.majko.domain.repository

import com.coolgirl.majko.commons.ApiResult
import com.coolgirl.majko.data.remote.dto.MessageData
import com.coolgirl.majko.data.remote.dto.ProjectData.*
import kotlinx.coroutines.flow.Flow

interface MajkoProjectRepositoryInterface {

    fun getPersonalProject(): Flow<ApiResult<List<ProjectDataResponse>>>

    fun getGroupProject(): Flow<ApiResult<List<ProjectDataResponse>>>

    fun postNewProject(project: ProjectData): Flow<ApiResult<ProjectDataResponse>>

    fun getProjectById(projectById: ProjectById): Flow<ApiResult<ProjectCurrentResponse>>

    fun updateProject(project: ProjectUpdate): Flow<ApiResult<ProjectCurrentResponse>>

    fun removeProject(projectId: ProjectById): Flow<ApiResult<Unit>>

    fun createInvitetoProject(projectById: ProjectByIdUnderscore): Flow<ApiResult<ProjectCreateInviteResponse>>

    fun joinByInvitation(invite: JoinByInviteProjectData): Flow<ApiResult<MessageData>>
}