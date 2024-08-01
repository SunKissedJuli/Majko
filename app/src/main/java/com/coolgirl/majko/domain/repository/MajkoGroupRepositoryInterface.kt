package com.coolgirl.majko.domain.repository

import com.coolgirl.majko.data.remote.ApiResult
import com.coolgirl.majko.data.remote.dto.GroupData.*
import com.coolgirl.majko.data.remote.dto.MessageData
import com.coolgirl.majko.data.remote.dto.ProjectData.JoinByInviteProjectData
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectDataResponse
import kotlinx.coroutines.flow.Flow

interface MajkoGroupRepositoryInterface {

    fun addGroup(group: GroupData) : Flow<ApiResult<GroupResponse>>

    fun getPersonalGroup() : Flow<ApiResult<List<GroupResponse>>>

    fun getGroupGroup() : Flow<ApiResult<List<GroupResponse>>>

    fun getGroupById(groupId: GroupById) : Flow<ApiResult<GroupResponse>>

    fun updateGroup(group: GroupUpdate) : Flow<ApiResult<GroupResponse>>

    fun removeGroup(groupId: GroupById) : Flow<ApiResult<Unit>>

    fun addProjectInGroup(group: ProjectInGroup) : Flow<ApiResult<ProjectDataResponse>>

    fun createInvitetoGroup(groupId: GroupByIdUnderscore) : Flow<ApiResult<GroupInviteResponse>>

    fun joinGroupByInvitation(invite: JoinByInviteProjectData) : Flow<ApiResult<MessageData>>
}