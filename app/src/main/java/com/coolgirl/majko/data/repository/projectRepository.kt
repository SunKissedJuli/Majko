package com.coolgirl.majko.data.repository

import com.coolgirl.majko.data.dataUi.MessageDataUi
import com.coolgirl.majko.data.dataUi.ProjectData.ProjectCreateInviteResponseUi
import com.coolgirl.majko.data.dataUi.ProjectData.ProjectCurrentResponseUi
import com.coolgirl.majko.data.dataUi.ProjectData.ProjectDataResponseUi
import com.coolgirl.majko.data.dataUi.ProjectData.toUi
import com.coolgirl.majko.data.dataUi.TaskData.toUi
import com.coolgirl.majko.data.dataUi.toUi
import com.coolgirl.majko.data.remote.*
import com.coolgirl.majko.data.remote.dto.MessageData
import com.coolgirl.majko.data.remote.dto.ProjectData.*
import com.coolgirl.majko.data.remote.dto.TaskData.SearchTask
import com.coolgirl.majko.domain.repository.MajkoProjectRepositoryInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MajkoProjectRepository @Inject constructor(
    private val api: ApiMajko
):MajkoProjectRepositoryInterface{
    override fun getPersonalProject(search: SearchTask): Flow<ApiResult<List<ProjectDataResponseUi>>> = flow {
        val apiResult = handler { api.getPersonalProject(search) }
        val uiResult = apiResult.mapList { it.toUi() }
        emit(uiResult)
    }

    override fun getGroupProject(search: SearchTask): Flow<ApiResult<List<ProjectDataResponseUi>>> = flow {
        val apiResult = handler { api.getGroupProject(search) }
        val uiResult = apiResult.mapList { it.toUi() }
        emit(uiResult)
    }

    override fun postNewProject(project: ProjectData): Flow<ApiResult<ProjectDataResponseUi>> = flow {
        val apiResult = handler { api.postNewProject(project) }
        val uiResult = apiResult.map { it.toUi() }
        emit(uiResult)
    }

    override fun getProjectById(projectById: ProjectById): Flow<ApiResult<ProjectCurrentResponseUi>> = flow {
        val apiResult = handler { api.getProjectById(projectById) }
        val uiResult = apiResult.map { it.toUi() }
        emit(uiResult)
    }

    override fun updateProject(project: ProjectUpdate): Flow<ApiResult<ProjectCurrentResponseUi>> = flow {
        val apiResult = handler { api.updateProject(project) }
        val uiResult = apiResult.map { it.toUi() }
        emit(uiResult)
    }

    override fun removeProject(projectId: ProjectById): Flow<ApiResult<Unit>> = flow {
        emit(handler { api.removeProject(projectId)})
    }

    override fun createInvitetoProject(projectById: ProjectByIdUnderscore): Flow<ApiResult<ProjectCreateInviteResponseUi>> = flow {
        val apiResult = handler { api.createInvitetoProject(projectById) }
        val uiResult = apiResult.map { it.toUi() }
        emit(uiResult)
    }

    override fun joinByInvitation(invite: JoinByInviteProjectData): Flow<ApiResult<MessageDataUi>> = flow {
        val apiResult = handler { api.joinByInvitation(invite) }
        val uiResult = apiResult.map { it.toUi() }
        emit(uiResult)
    }
}