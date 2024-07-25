package com.coolgirl.majko.data

import com.coolgirl.majko.commons.ApiResult
import com.coolgirl.majko.data.remote.ApiMajko
import com.coolgirl.majko.data.remote.dto.*
import com.coolgirl.majko.data.remote.dto.GroupData.*
import com.coolgirl.majko.data.remote.dto.Info.Info
import com.coolgirl.majko.data.remote.dto.NoteData.NoteById
import com.coolgirl.majko.data.remote.dto.NoteData.NoteData
import com.coolgirl.majko.data.remote.dto.NoteData.NoteDataResponse
import com.coolgirl.majko.data.remote.dto.NoteData.NoteUpdate
import com.coolgirl.majko.data.remote.dto.ProjectData.*
import com.coolgirl.majko.data.remote.dto.TaskData.*
import com.coolgirl.majko.data.remote.dto.UserSignUpData.UserSignUpData
import com.coolgirl.majko.data.remote.dto.UserSignUpData.UserSignUpDataResponse
import com.coolgirl.majko.data.remote.handler
import com.coolgirl.majko.domain.repository.MajkoRepositoryInterface
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MajkoRepository @Inject constructor(
    private val api: ApiMajko
): MajkoRepositoryInterface {

    //юзер и авторизация
    override fun signIn(user: UserSignInData?): Flow<ApiResult<UserSignInDataResponse>> = flow {
        emit(handler { api.signIn(user) })
    }

    override fun currentUser(token: String): Flow<ApiResult<CurrentUserDataResponse>> = flow {
        emit(handler { api.currentUser(token) })
    }

    override fun signUp(user: UserSignUpData?): Flow<ApiResult<UserSignUpDataResponse>> = flow {
        emit(handler { api.signUp(user) })
    }

    override fun updateUserName(token: String, user: UserUpdateName): Flow<ApiResult<CurrentUserDataResponse>> = flow {
        emit(handler { api.updateUserName(token, user) })
    }

    override fun updateUserEmail(token: String, user: UserUpdateEmail): Flow<ApiResult<CurrentUserDataResponse>> = flow {
        emit(handler { api.updateUserEmail(token, user) })
    }

    override fun updateUserPassword(token: String, user: UserUpdatePassword): Flow<ApiResult<CurrentUserDataResponse>> = flow {
        emit(handler { api.updateUserPassword(token, user) })
    }

    override fun updateUserImage(token: String, user: UserUpdateImage): Flow<ApiResult<CurrentUserDataResponse>> = flow {
        emit(handler { api.updateUserImage(token, user) })
    }

    //таски
    override fun getAllUserTask(token: String): Flow<ApiResult<List<TaskDataResponse>>> = flow {
        emit(handler { api.getAllUserTask(token) })
    }

    override fun postNewTask(token: String, task: TaskData): Flow<ApiResult<TaskDataResponse>> = flow {
        emit(handler { api.postNewTask(token, task) })
    }

    override fun getTaskById(token: String, taskId: TaskById): Flow<ApiResult<TaskDataResponse>> = flow {
        emit(handler { api.getTaskById(token, taskId) })
    }

    override fun updateTask(token: String, taskData: TaskUpdateData) : Flow<ApiResult<TaskDataResponse>> = flow {
        emit(handler { api.updateTask(token, taskData) })
    }

    override fun removeTask(token: String, taskId: TaskBy_Id): Flow<ApiResult<Unit>> = flow {
        emit(handler { api.removeTask(token, taskId) })
    }

    override fun getSubtask(token: String, taskId: TaskById): Flow<ApiResult<List<TaskDataResponse>>> = flow {
        emit(handler { api.getSubtask(token, taskId) })
    }

    //фавориты
    override fun removeFavotire(token: String, taskId: TaskById): Flow<ApiResult<MessageData>> = flow {
        emit(handler { api.removeFavotire(token, taskId) })
    }

    override fun addToFavorite(token: String, taskId: TaskById): Flow<ApiResult<MessageData>> = flow {
        emit(handler { api.addToFavorite(token, taskId) })
    }

    override fun getAllFavorites(token: String): Flow<ApiResult<List<TaskDataResponse>>> = flow {
        emit(handler { api.getAllFavorites(token)})
    }

    override fun getPersonalProject(token: String): Flow<ApiResult<List<ProjectDataResponse>>> = flow {
        emit(handler { api.getPersonalProject(token)})
    }

    override fun getGroupProject(token: String): Flow<ApiResult<List<ProjectDataResponse>>> = flow {
        emit(handler { api.getGroupProject(token)})
    }

    override fun postNewProject(token: String, project: ProjectData): Flow<ApiResult<ProjectDataResponse>> = flow {
        emit(handler { api.postNewProject(token, project)})
    }

    override fun getProjectById(token: String, projectById: ProjectById):  Flow<ApiResult<ProjectCurrentResponse>> = flow {
        emit(handler { api.getProjectById(token, projectById)})
    }

    override fun updateProject(token: String, project: ProjectUpdate):  Flow<ApiResult<ProjectCurrentResponse>> = flow {
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


    //записи
    override fun addNote(token: String, note: NoteData): Flow<ApiResult<NoteDataResponse>> = flow {
        emit(handler { api.addNote(token, note)})
    }

    override fun updateNote(token: String, note: NoteUpdate): Flow<ApiResult<NoteDataResponse>> = flow {
        emit(handler { api.updateNote(token, note)})
    }

    override fun removeNote(token: String, noteId: NoteById): Flow<ApiResult<Unit>> = flow {
        emit(handler { api.removeNote(token, noteId)})
    }

    override fun getNotes(token: String, taskId: TaskById): Flow<ApiResult<List<NoteDataResponse>>> = flow {
        emit(handler { api.getNotes(token, taskId)})
    }


    //группы
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


    //инфо
    override fun getPriorities(): Flow<ApiResult<List<Info>>> = flow {
        emit(handler { api.getPriorities()})
    }

    override fun getStatuses(): Flow<ApiResult<List<Info>>> = flow {
        emit(handler { api.getStatuses()})
    }

}
