package com.coolgirl.majko.data.repository

import com.coolgirl.majko.data.remote.ApiMajko
import com.coolgirl.majko.data.remote.ApiResult
import com.coolgirl.majko.data.remote.dto.GroupData.*
import com.coolgirl.majko.data.remote.dto.MessageData
import com.coolgirl.majko.data.remote.dto.ProjectData.JoinByInviteProjectData
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectDataResponse
import com.coolgirl.majko.data.remote.dto.TaskData.SearchTask
import com.coolgirl.majko.data.remote.handler
import com.coolgirl.majko.domain.repository.MajkoGroupRepositoryInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MajkoGroupRepository @Inject constructor(
    private val api: ApiMajko
): MajkoGroupRepositoryInterface {

    override fun addGroup(group: GroupData): Flow<ApiResult<GroupResponse>> = flow {
        emit(handler { api.addGroup(group)})
    }

    override fun getPersonalGroup(search: SearchTask): Flow<ApiResult<List<GroupResponse>>> = flow {
        emit(handler { api.getPersonalGroup(search)})
    }

    override fun getGroupGroup(search: SearchTask): Flow<ApiResult<List<GroupResponse>>> = flow {
        emit(handler { api.getGroupGroup(search)})
    }

    override fun getGroupById(groupId: GroupById): Flow<ApiResult<GroupResponse>> = flow {
        emit(handler { api.getGroupById(groupId)})
    }

    override fun updateGroup(group: GroupUpdate): Flow<ApiResult<GroupResponse>> = flow {
        emit(handler { api.updateGroup(group)})
    }

    override fun removeGroup(groupId: GroupById): Flow<ApiResult<Unit>> = flow {
        emit(handler { api.removeGroup(groupId)})
    }

    override fun addProjectInGroup(group: ProjectInGroup): Flow<ApiResult<ProjectDataResponse>> = flow {
        emit(handler { api.addProjectInGroup(group)})
    }

    override fun createInvitetoGroup(groupId: GroupByIdUnderscore): Flow<ApiResult<GroupInviteResponse>> = flow {
        emit(handler { api.createInvitetoGroup(groupId)})
    }

    override fun joinGroupByInvitation(invite: JoinByInviteProjectData): Flow<ApiResult<MessageData>> = flow {
        emit(handler { api.joinGroupByInvitation(invite)})
    }

}