package com.coolgirl.majko.data.remote

import com.coolgirl.majko.data.dataUi.User.CurrentUserDataResponseUi
import com.coolgirl.majko.data.remote.dto.*
import com.coolgirl.majko.data.remote.dto.GroupData.*
import com.coolgirl.majko.data.remote.dto.Info.Info
import com.coolgirl.majko.data.remote.dto.NoteData.NoteById
import com.coolgirl.majko.data.remote.dto.NoteData.NoteData
import com.coolgirl.majko.data.remote.dto.NoteData.NoteDataResponse
import com.coolgirl.majko.data.remote.dto.NoteData.NoteUpdate
import com.coolgirl.majko.data.remote.dto.ProjectData.*
import com.coolgirl.majko.data.remote.dto.TaskData.*
import com.coolgirl.majko.data.remote.dto.User.CurrentUserDataResponse
import com.coolgirl.majko.data.remote.dto.UserSignUpData.UserSignUpData
import com.coolgirl.majko.data.remote.dto.UserSignUpData.UserSignUpDataResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiMajko{

    //юзер и авторизация
    @POST("api/auth/local/signin")
    suspend fun signIn(@Body user: UserSignInData?): Response<UserSignInDataResponse>

    @POST("api/auth/local/signup")
    suspend fun signUp(@Body user: UserSignUpData?): Response<UserSignUpDataResponse>

    @GET("api/user/current")
    suspend fun currentUser(): Response<CurrentUserDataResponse>

    @POST("api/user/update")
    suspend fun updateUserName(@Body user: UserUpdateName): Response<CurrentUserDataResponse>

    @POST("api/user/update")
    suspend fun updateUserEmail(@Body user: UserUpdateEmail): Response<CurrentUserDataResponse>

    @POST("api/user/update")
    suspend fun updateUserPassword(@Body user: UserUpdatePassword): Response<CurrentUserDataResponse>

    @POST("api/user/update")
    suspend fun updateUserImage(@Body user: UserUpdateImage): Response<CurrentUserDataResponse>

    //таски
    @POST("api/task/allUserTasks")
    suspend fun getAllUserTask(@Body search: SearchTask): Response<List<TaskDataResponse>>

    @POST("api/task/create")
    suspend fun postNewTask(@Body task:TaskData): Response<TaskDataResponse>

    @POST("api/task/getById")
    suspend fun getTaskById(@Body taskId: TaskById): Response<TaskDataResponse>

    @HTTP(method = "DELETE", path = "api/task/delete", hasBody = true)
    suspend fun removeTask(@Body taskId: TaskByIdUnderscore) : Response<Unit>

    @POST("api/task/update")
    suspend fun updateTask(@Body taskData: TaskUpdateData) : Response<TaskDataResponse>

    @POST("api/task/getSubtaskForTask")
    suspend fun getSubtask(@Body taskId: TaskById): Response<List<TaskDataResponse>>


    //фавориты
    @HTTP(method = "DELETE", path = "api/task/removeFavorite", hasBody = true)
    suspend fun removeFavotire(@Body taskId: TaskById) : Response<MessageData>

    @POST("api/task/addToFavorite")
    suspend fun addToFavorite(@Body taskId: TaskById) : Response<MessageData>

    @GET("api/task/favorites")
    suspend fun getAllFavorites(): Response<List<TaskDataResponse>>


    //проекты
    @POST("api/project/getPersonal")
    suspend fun getPersonalProject(@Body search: SearchTask): Response<List<ProjectDataResponse>>

    @POST("api/project/getPrivate")
    suspend fun getGroupProject(@Body search: SearchTask): Response<List<ProjectDataResponse>>

    @POST("api/project/create")
    suspend fun postNewProject(@Body project:ProjectData): Response<ProjectDataResponse>

    @POST("api/project/getById")
    suspend fun getProjectById(@Body projectById: ProjectById) : Response<ProjectCurrentResponse>

    @POST("api/project/update")
    suspend fun updateProject(@Body project: ProjectUpdate) : Response<ProjectCurrentResponse>

    @HTTP(method = "DELETE", path = "api/project/delete", hasBody = true)
    suspend fun removeProject(@Body projectId: ProjectById) : Response<Unit>

    @POST("api/project/createInvite")
    suspend fun createInvitetoProject(@Body projectById: ProjectByIdUnderscore) : Response<ProjectCreateInviteResponse>

    @POST("api/project/joinByInvitation")
    suspend fun joinByInvitation(@Body invite: JoinByInviteProjectData) : Response<MessageData>


    //записи
    @POST("api/note/create")
    suspend fun addNote(@Body note: NoteData) : Response<NoteDataResponse>

    @POST("api/note/update")
    suspend fun updateNote(@Body note: NoteUpdate) : Response<NoteDataResponse>

    @HTTP(method = "DELETE", path = "api/note/delete", hasBody = true)
    suspend fun removeNote(@Body noteId: NoteById) : Response<Unit>

    @POST("api/note/getNotes?aseae=asdsad")
    suspend fun getNotes(@Body taskId: TaskById) : Response<List<NoteDataResponse>>


    //группы
    @POST("api/group/create")
    suspend fun addGroup(@Body group: GroupData) : Response<GroupResponse>

    @POST("api/group/getPersonal")
    suspend fun getPersonalGroup(@Body search: SearchTask): Response<List<GroupResponse>>

    @POST("api/group/getPrivate")
    suspend fun getGroupGroup(@Body search: SearchTask): Response<List<GroupResponse>>

    @POST("api/group/getById")
    suspend fun getGroupById(@Body groupId: GroupById): Response<GroupResponse>

    @POST("api/group/update")
    suspend fun updateGroup(@Body group: GroupUpdate): Response<GroupResponse>

    @HTTP(method = "DELETE", path = "api/group/delete", hasBody = true)
    suspend fun removeGroup(@Body groupId: GroupById) : Response<Unit>

    @POST("api/group/addProjectInGroup")
    suspend fun addProjectInGroup(@Body group: ProjectInGroup): Response<ProjectDataResponse>

    @POST("api/group/createInvite")
    suspend fun createInvitetoGroup(@Body groupId: GroupByIdUnderscore) : Response<GroupInviteResponse>

    @POST("api/group/joinByInvitation")
    suspend fun joinGroupByInvitation(@Body invite: JoinByInviteProjectData) : Response<MessageData>


    //инфо
    @GET("api/get_statuses")
    suspend fun getStatuses() : Response<List<Info>>

    @GET("api/get_priorities")
    suspend fun getPriorities() : Response<List<Info>>

}