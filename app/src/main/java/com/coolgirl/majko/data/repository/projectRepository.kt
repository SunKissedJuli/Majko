package com.coolgirl.majko.data.repository

import com.coolgirl.majko.commons.ApiResult
import com.coolgirl.majko.data.remote.ApiMajko
import com.coolgirl.majko.data.remote.dto.MessageData
import com.coolgirl.majko.data.remote.dto.ProjectData.*
import com.coolgirl.majko.data.remote.handler
import com.coolgirl.majko.domain.repository.MajkoProjectRepositoryInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MajkoProjectRepository @Inject constructor(
    private val api: ApiMajko
):MajkoProjectRepositoryInterface{
    override fun getPersonalProject(): Flow<ApiResult<List<ProjectDataResponse>>> = flow {
        emit(handler { api.getPersonalProject()})
    }

    override fun getGroupProject(): Flow<ApiResult<List<ProjectDataResponse>>> = flow {
        emit(handler { api.getGroupProject()})
    }

    override fun postNewProject(project: ProjectData): Flow<ApiResult<ProjectDataResponse>> = flow {
        emit(handler { api.postNewProject(project)})
    }

    override fun getProjectById(projectById: ProjectById): Flow<ApiResult<ProjectCurrentResponse>> = flow {
        emit(handler { api.getProjectById(projectById)})
    }

    override fun updateProject(project: ProjectUpdate): Flow<ApiResult<ProjectCurrentResponse>> = flow {
        emit(handler { api.updateProject(project)})
    }

    override fun removeProject(projectId: ProjectById): Flow<ApiResult<Unit>> = flow {
        emit(handler { api.removeProject(projectId)})
    }

    override fun createInvitetoProject(projectById: ProjectByIdUnderscore): Flow<ApiResult<ProjectCreateInviteResponse>> = flow {
        emit(handler { api.createInvitetoProject(projectById)})
    }

    override fun joinByInvitation(invite: JoinByInviteProjectData): Flow<ApiResult<MessageData>> = flow {
        emit(handler { api.joinByInvitation(invite)})
    }
}