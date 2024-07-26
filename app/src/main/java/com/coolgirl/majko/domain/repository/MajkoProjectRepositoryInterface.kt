package com.coolgirl.majko.domain.repository

import com.coolgirl.majko.commons.ApiResult
import com.coolgirl.majko.data.remote.dto.MessageData
import com.coolgirl.majko.data.remote.dto.ProjectData.*
import kotlinx.coroutines.flow.Flow

interface MajkoProjectRepositoryInterface {

    fun getPersonalProject(token: String): Flow<ApiResult<List<ProjectDataResponse>>>

    fun getGroupProject(token: String): Flow<ApiResult<List<ProjectDataResponse>>>

    fun postNewProject(token: String, project: ProjectData): Flow<ApiResult<ProjectDataResponse>>

    fun getProjectById(token: String, projectById: ProjectById): Flow<ApiResult<ProjectCurrentResponse>>

    fun updateProject(token: String, project: ProjectUpdate): Flow<ApiResult<ProjectCurrentResponse>>

    fun removeProject(token: String, projectId: ProjectById): Flow<ApiResult<Unit>>

    fun createInvitetoProject(token: String, projectById: ProjectBy_Id): Flow<ApiResult<ProjectCreateInviteResponse>>

    fun joinByInvitation(token: String, invite: JoinByInviteProjectData): Flow<ApiResult<MessageData>>
}