package com.coolgirl.majko.data.repository

import com.coolgirl.majko.data.dataUi.GroupData.GroupInviteResponseUi
import com.coolgirl.majko.data.dataUi.GroupData.GroupResponseUi
import com.coolgirl.majko.data.dataUi.GroupData.toUi
import com.coolgirl.majko.data.dataUi.MessageDataUi
import com.coolgirl.majko.data.dataUi.ProjectData.ProjectDataResponseUi
import com.coolgirl.majko.data.dataUi.ProjectData.toUi
import com.coolgirl.majko.data.dataUi.toUi
import com.coolgirl.majko.data.remote.*
import com.coolgirl.majko.data.remote.dto.GroupData.*
import com.coolgirl.majko.data.remote.dto.MessageData
import com.coolgirl.majko.data.remote.dto.ProjectData.JoinByInviteProjectData
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectDataResponse
import com.coolgirl.majko.data.remote.dto.TaskData.SearchTask
import com.coolgirl.majko.domain.repository.MajkoGroupRepositoryInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MajkoGroupRepository @Inject constructor(
    private val api: ApiMajko
): MajkoGroupRepositoryInterface {

    override fun addGroup(group: GroupData): Flow<ApiResult<GroupResponseUi>> = flow {
        val apiResult = handler { api.addGroup(group) }
        val uiResult = apiResult.map { it.toUi() }
        emit(uiResult)
    }

    override fun getPersonalGroup(search: SearchTask): Flow<ApiResult<List<GroupResponseUi>>> = flow {
        val apiResult = handler { api.getPersonalGroup(search) }
        val uiResult = apiResult.mapList { it.toUi() }
        emit(uiResult)
    }

    override fun getGroupGroup(search: SearchTask): Flow<ApiResult<List<GroupResponseUi>>> = flow {
        val apiResult = handler { api.getGroupGroup(search) }
        val uiResult = apiResult.mapList { it.toUi() }
        emit(uiResult)
    }

    override fun getGroupById(groupId: GroupById): Flow<ApiResult<GroupResponseUi>> = flow {
        val apiResult = handler { api.getGroupById(groupId) }
        val uiResult = apiResult.map { it.toUi() }
        emit(uiResult)
    }

    override fun updateGroup(group: GroupUpdate): Flow<ApiResult<GroupResponseUi>> = flow {
        val apiResult = handler { api.updateGroup(group) }
        val uiResult = apiResult.map { it.toUi() }
        emit(uiResult)
    }

    override fun removeGroup(groupId: GroupById): Flow<ApiResult<Unit>> = flow {
        emit(handler { api.removeGroup(groupId)})
    }

    override fun addProjectInGroup(group: ProjectInGroup): Flow<ApiResult<ProjectDataResponseUi>> = flow {
        val apiResult = handler { api.addProjectInGroup(group) }
        val uiResult = apiResult.map { it.toUi() }
        emit(uiResult)
    }

    override fun createInvitetoGroup(groupId: GroupByIdUnderscore): Flow<ApiResult<GroupInviteResponseUi>> = flow {
        val apiResult = handler { api.createInvitetoGroup(groupId) }
        val uiResult = apiResult.map { it.toUi() }
        emit(uiResult)
    }

    override fun joinGroupByInvitation(invite: JoinByInviteProjectData): Flow<ApiResult<MessageDataUi>> = flow {
        val apiResult = handler { api.joinGroupByInvitation(invite) }
        val uiResult = apiResult.map { it.toUi() }
        emit(uiResult)
    }

}