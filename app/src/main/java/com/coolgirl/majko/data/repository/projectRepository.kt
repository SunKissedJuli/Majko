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
    override fun getPersonalProject(token: String): Flow<ApiResult<List<ProjectDataResponse>>> = flow {
        emit(handler { api.getPersonalProject(token)})
    }

    override fun getGroupProject(token: String): Flow<ApiResult<List<ProjectDataResponse>>> = flow {
        emit(handler { api.getGroupProject(token)})
    }

    override fun postNewProject(token: String, project: ProjectData): Flow<ApiResult<ProjectDataResponse>> = flow {
        emit(handler { api.postNewProject(token, project)})
    }

    override fun getProjectById(token: String, projectById: ProjectById): Flow<ApiResult<ProjectCurrentResponse>> = flow {
        emit(handler { api.getProjectById(token, projectById)})
    }

    override fun updateProject(token: String, project: ProjectUpdate): Flow<ApiResult<ProjectCurrentResponse>> = flow {
        emit(handler { api.updateProject(token, project)})
    }

    override fun removeProject(token: String, projectId: ProjectById): Flow<ApiResult<Unit>> = flow {
        emit(handler { api.removeProject(token, projectId)})
    }

    override fun createInvitetoProject(token: String, projectById: ProjectBy_Id): Flow<ApiResult<ProjectCreateInviteResponse>> = flow {
        emit(handler { api.createInvitetoProject(token, projectById)})
    }

    override fun joinByInvitation(token: String, invite: JoinByInviteProjectData): Flow<ApiResult<MessageData>> = flow {
        emit(handler { api.joinByInvitation(token, invite)})
    }
}