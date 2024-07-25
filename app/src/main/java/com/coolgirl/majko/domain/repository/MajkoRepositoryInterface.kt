package com.coolgirl.majko.domain.repository

import com.coolgirl.majko.commons.ApiResult
import com.coolgirl.majko.data.remote.dto.*
import com.coolgirl.majko.data.remote.dto.GroupData.*
import com.coolgirl.majko.data.remote.dto.NoteData.NoteById
import com.coolgirl.majko.data.remote.dto.NoteData.NoteData
import com.coolgirl.majko.data.remote.dto.NoteData.NoteDataResponse
import com.coolgirl.majko.data.remote.dto.NoteData.NoteUpdate
import com.coolgirl.majko.data.remote.dto.ProjectData.*
import com.coolgirl.majko.data.remote.dto.TaskData.*
import com.coolgirl.majko.data.remote.dto.UserSignUpData.UserSignUpData
import com.coolgirl.majko.data.remote.dto.UserSignUpData.UserSignUpDataResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.*

interface MajkoRepositoryInterface {

   //юзер и авторизация
   fun signIn(user: UserSignInData?): Flow<ApiResult<UserSignInDataResponse>>

   fun currentUser(token: String): Flow<ApiResult<CurrentUserDataResponse>>

   fun signUp(user: UserSignUpData?): Flow<ApiResult<UserSignUpDataResponse>>

   fun updateUserName(token: String, user: UserUpdateName): Flow<ApiResult<CurrentUserDataResponse>>

   fun updateUserEmail(token: String, user: UserUpdateEmail): Flow<ApiResult<CurrentUserDataResponse>>

   fun updateUserPassword(token: String, user: UserUpdatePassword): Flow<ApiResult<CurrentUserDataResponse>>

   fun updateUserImage(token: String, user: UserUpdateImage): Flow<ApiResult<CurrentUserDataResponse>>

   //таски
   fun getAllUserTask(token: String): Flow<ApiResult<List<TaskDataResponse>>>

   fun postNewTask(token: String, task: TaskData): Flow<ApiResult<TaskDataResponse>>

   fun getTaskById(token: String, taskId: TaskById): Flow<ApiResult<TaskDataResponse>>

   fun removeTask(token: String, taskId: TaskBy_Id): Flow<ApiResult<Unit>>

   fun updateTask(token: String, taskData: TaskUpdateData): Flow<ApiResult<TaskDataResponse>>

   fun getSubtask(token: String, taskId: TaskById): Flow<ApiResult<List<TaskDataResponse>>>

   //фавориты
   fun removeFavotire(token: String, taskId: TaskById): Flow<ApiResult<MessageData>>

   fun addToFavorite(token: String, taskId: TaskById): Flow<ApiResult<MessageData>>

   fun getAllFavorites(token: String): Flow<ApiResult<List<TaskDataResponse>>>

   //проекты
   fun getPersonalProject(token: String): Flow<ApiResult<List<ProjectDataResponse>>>

   fun getGroupProject(token: String): Flow<ApiResult<List<ProjectDataResponse>>>

   fun postNewProject(token: String, project: ProjectData): Flow<ApiResult<ProjectDataResponse>>

   fun getProjectById(token: String, projectById: ProjectById): Flow<ApiResult<ProjectCurrentResponse>>

   fun updateProject(token: String, project: ProjectUpdate): Flow<ApiResult<ProjectCurrentResponse>>

   fun removeProject(token: String, projectId: ProjectById): Flow<ApiResult<Unit>>

   fun createInvitetoProject(token: String, projectById: ProjectBy_Id): Flow<ApiResult<ProjectCreateInviteResponse>>

   fun joinByInvitation(token: String, invite: JoinByInviteProjectData): Flow<ApiResult<MessageData>>


   //записи
   fun addNote(token: String, note: NoteData) : Flow<ApiResult<NoteDataResponse>>

   fun updateNote(token: String, note: NoteUpdate) : Flow<ApiResult<NoteDataResponse>>

   fun removeNote(token: String, noteId: NoteById) : Flow<ApiResult<Unit>>

   fun getNotes(token: String, taskId: TaskById) : Flow<ApiResult<List<NoteDataResponse>>>


   //группы
   fun addGroup(token: String, group: GroupData) : Flow<ApiResult<GroupResponse>>

   fun getPersonalGroup(token: String) : Flow<ApiResult<List<GroupResponse>>>

   fun getGroupGroup(token: String) : Flow<ApiResult<List<GroupResponse>>>

   fun getGroupById(token: String, groupId: GroupById) :Flow<ApiResult<GroupResponse>>

   fun updateGroup(token: String, group: GroupUpdate) : Flow<ApiResult<GroupResponse>>

   fun removeGroup(token: String,  groupId: GroupById) : Flow<ApiResult<Unit>>

   fun addProjectInGroup(token: String, group: ProjectInGroup) : Flow<ApiResult<ProjectDataResponse>>

   fun createInvitetoGroup(token: String,  groupId: GroupBy_Id) : Flow<ApiResult<GroupInviteResponse>>

   fun joinGroupByInvitation(token: String,  invite: JoinByInviteProjectData) : Flow<ApiResult<MessageData>>





}