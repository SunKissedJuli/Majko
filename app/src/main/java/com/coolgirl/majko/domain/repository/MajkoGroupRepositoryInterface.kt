package com.coolgirl.majko.domain.repository

import com.coolgirl.majko.data.dataUi.GroupData.GroupInviteResponseUi
import com.coolgirl.majko.data.dataUi.GroupData.GroupResponseUi
import com.coolgirl.majko.data.dataUi.MessageDataUi
import com.coolgirl.majko.data.dataUi.ProjectData.ProjectDataResponseUi
import com.coolgirl.majko.data.remote.ApiResult
import com.coolgirl.majko.data.remote.dto.GroupData.*
import com.coolgirl.majko.data.remote.dto.MessageData
import com.coolgirl.majko.data.remote.dto.ProjectData.JoinByInviteProjectData
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectDataResponse
import com.coolgirl.majko.data.remote.dto.TaskData.SearchTask
import kotlinx.coroutines.flow.Flow

interface MajkoGroupRepositoryInterface {

    fun addGroup(group: GroupData) : Flow<ApiResult<GroupResponseUi>>

    fun getPersonalGroup(search: SearchTask) : Flow<ApiResult<List<GroupResponseUi>>>

    fun getGroupGroup(search: SearchTask) : Flow<ApiResult<List<GroupResponseUi>>>

    fun getGroupById(groupId: GroupById) : Flow<ApiResult<GroupResponseUi>>

    fun updateGroup(group: GroupUpdate) : Flow<ApiResult<GroupResponseUi>>

    fun removeGroup(groupId: GroupById) : Flow<ApiResult<Unit>>

    fun addProjectInGroup(group: ProjectInGroup) : Flow<ApiResult<ProjectDataResponseUi>>

    fun createInvitetoGroup(groupId: GroupByIdUnderscore) : Flow<ApiResult<GroupInviteResponseUi>>

    fun joinGroupByInvitation(invite: JoinByInviteProjectData) : Flow<ApiResult<MessageDataUi>>
}