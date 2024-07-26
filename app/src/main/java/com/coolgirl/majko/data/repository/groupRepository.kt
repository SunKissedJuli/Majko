package com.coolgirl.majko.data.repository

import com.coolgirl.majko.commons.ApiResult
import com.coolgirl.majko.data.remote.ApiMajko
import com.coolgirl.majko.data.remote.dto.GroupData.*
import com.coolgirl.majko.data.remote.dto.MessageData
import com.coolgirl.majko.data.remote.dto.ProjectData.JoinByInviteProjectData
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectDataResponse
import com.coolgirl.majko.data.remote.handler
import com.coolgirl.majko.domain.repository.MajkoGroupRepositoryInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MajkoGroupRepository @Inject constructor(
    private val api: ApiMajko
): MajkoGroupRepositoryInterface {

    override fun addGroup(token: String, group: GroupData): Flow<ApiResult<GroupResponse>> = flow {
        emit(handler { api.addGroup(token, group)})
    }

    override fun getPersonalGroup(token: String): Flow<ApiResult<List<GroupResponse>>> = flow {
        emit(handler { api.getPersonalGroup(token)})
    }

    override fun getGroupGroup(token: String): Flow<ApiResult<List<GroupResponse>>> = flow {
        emit(handler { api.getGroupGroup(token)})
    }

    override fun getGroupById(token: String, groupId: GroupById): Flow<ApiResult<GroupResponse>> = flow {
        emit(handler { api.getGroupById(token, groupId)})
    }

    override fun updateGroup(token: String, group: GroupUpdate): Flow<ApiResult<GroupResponse>> = flow {
        emit(handler { api.updateGroup(token, group)})
    }

    override fun removeGroup(token: String, groupId: GroupById): Flow<ApiResult<Unit>> = flow {
        emit(handler { api.removeGroup(token, groupId)})
    }

    override fun addProjectInGroup(token: String, group: ProjectInGroup): Flow<ApiResult<ProjectDataResponse>> = flow {
        emit(handler { api.addProjectInGroup(token, group)})
    }

    override fun createInvitetoGroup(token: String, groupId: GroupBy_Id): Flow<ApiResult<GroupInviteResponse>> = flow {
        emit(handler { api.createInvitetoGroup(token, groupId)})
    }

    override fun joinGroupByInvitation(token: String, invite: JoinByInviteProjectData): Flow<ApiResult<MessageData>> = flow {
        emit(handler { api.joinGroupByInvitation(token, invite)})
    }

}