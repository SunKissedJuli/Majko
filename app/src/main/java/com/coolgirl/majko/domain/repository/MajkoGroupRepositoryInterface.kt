package com.coolgirl.majko.domain.repository

import com.coolgirl.majko.commons.ApiResult
import com.coolgirl.majko.data.remote.dto.GroupData.*
import com.coolgirl.majko.data.remote.dto.MessageData
import com.coolgirl.majko.data.remote.dto.ProjectData.JoinByInviteProjectData
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectDataResponse
import kotlinx.coroutines.flow.Flow

interface MajkoGroupRepositoryInterface {

    fun addGroup(token: String, group: GroupData) : Flow<ApiResult<GroupResponse>>

    fun getPersonalGroup(token: String) : Flow<ApiResult<List<GroupResponse>>>

    fun getGroupGroup(token: String) : Flow<ApiResult<List<GroupResponse>>>

    fun getGroupById(token: String, groupId: GroupById) : Flow<ApiResult<GroupResponse>>

    fun updateGroup(token: String, group: GroupUpdate) : Flow<ApiResult<GroupResponse>>

    fun removeGroup(token: String,  groupId: GroupById) : Flow<ApiResult<Unit>>

    fun addProjectInGroup(token: String, group: ProjectInGroup) : Flow<ApiResult<ProjectDataResponse>>

    fun createInvitetoGroup(token: String,  groupId: GroupBy_Id) : Flow<ApiResult<GroupInviteResponse>>

    fun joinGroupByInvitation(token: String,  invite: JoinByInviteProjectData) : Flow<ApiResult<MessageData>>
}