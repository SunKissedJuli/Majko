package com.coolgirl.majko.data.remote

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
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiMajko{

    //юзер и авторизация
    @POST("api/auth/local/signin")
    suspend fun signIn(@Body user: UserSignInData?): Response<UserSignInDataResponse>

    @POST("api/auth/local/signup")
    suspend fun signUp(@Body user: UserSignUpData?): Response<UserSignUpDataResponse>

    @GET("api/user/current")
    suspend fun currentUser(@Header("Authorization") token: String): Response<CurrentUserDataResponse>

    @POST("api/user/update")
    suspend fun updateUserName(@Header("Authorization") token: String, @Body user: UserUpdateName): Response<CurrentUserDataResponse>

    @POST("api/user/update")
    suspend fun updateUserEmail(@Header("Authorization") token: String, @Body user: UserUpdateEmail): Response<CurrentUserDataResponse>

    @POST("api/user/update")
    suspend fun updateUserPassword(@Header("Authorization") token: String, @Body user: UserUpdatePassword): Response<CurrentUserDataResponse>

    @POST("api/user/update")
    suspend fun updateUserImage(@Header("Authorization") token: String, @Body user: UserUpdateImage): Response<CurrentUserDataResponse>

    //таски
    @POST("api/task/allUserTasks")
    suspend fun getAllUserTask(@Header("Authorization") token: String): Response<List<TaskDataResponse>>

    @POST("api/task/create")
    suspend fun postNewTask(@Header("Authorization") token: String, @Body task:TaskData): Response<TaskDataResponse>

    @POST("api/task/getById")
    suspend fun getTaskById(@Header("Authorization") token: String, @Body taskId: TaskById): Response<TaskDataResponse>

    @HTTP(method = "DELETE", path = "api/task/delete", hasBody = true)
    suspend fun removeTask(@Header("Authorization") token: String, @Body taskId: TaskBy_Id) : Response<Unit>

    @POST("api/task/update")
    suspend fun updateTask(@Header("Authorization") token: String, @Body taskData: TaskUpdateData) : Response<TaskDataResponse>

    @POST("api/task/getSubtaskForTask")
    suspend fun getSubtask(@Header("Authorization") token: String, @Body taskId: TaskById): Response<List<TaskDataResponse>>


    //фавориты
    @HTTP(method = "DELETE", path = "api/task/removeFavorite", hasBody = true)
    suspend fun removeFavotire(@Header("Authorization") token: String, @Body taskId: TaskById) : Response<MessageData>

    @POST("api/task/addToFavorite")
    suspend fun addToFavorite(@Header("Authorization") token: String, @Body taskId: TaskById) : Response<MessageData>

    @GET("api/task/favorites")
    suspend fun getAllFavorites(@Header("Authorization") token: String): Response<List<TaskDataResponse>>


    //проекты
    @POST("api/project/getPersonal")
    suspend fun getPersonalProject(@Header("Authorization") token: String): Response<List<ProjectDataResponse>>

    @POST("api/project/getPrivate")
    suspend fun getGroupProject(@Header("Authorization") token: String): Response<List<ProjectDataResponse>>

    @POST("api/project/create")
    suspend fun postNewProject(@Header("Authorization") token: String, @Body project:ProjectData): Response<ProjectDataResponse>

    @POST("api/project/getById")
    suspend fun getProjectById(@Header("Authorization") token: String, @Body projectById: ProjectById) : Response<ProjectCurrentResponse>

    @POST("api/project/update")
    suspend fun updateProject(@Header("Authorization") token: String, @Body project: ProjectUpdate) : Response<ProjectCurrentResponse>

    @HTTP(method = "DELETE", path = "api/project/delete", hasBody = true)
    suspend fun removeProject(@Header("Authorization") token: String, @Body projectId: ProjectById) : Response<Unit>

    @POST("api/project/createInvite")
    suspend fun createInvitetoProject(@Header("Authorization") token: String, @Body projectById: ProjectBy_Id) : Response<ProjectCreateInviteResponse>

    @POST("api/project/joinByInvitation")
    suspend fun joinByInvitation(@Header("Authorization") token: String, @Body invite: JoinByInviteProjectData) : Response<MessageData>


    //записи
    @POST("api/note/create")
    suspend fun addNote(@Header("Authorization") token: String, @Body note: NoteData) : Response<NoteDataResponse>

    @POST("api/note/update")
    suspend fun updateNote(@Header("Authorization") token: String, @Body note: NoteUpdate) : Response<NoteDataResponse>

    @HTTP(method = "DELETE", path = "api/note/delete", hasBody = true)
    suspend fun removeNote(@Header("Authorization") token: String, @Body noteId: NoteById) : Response<Unit>

    @POST("api/note/getNotes?aseae=asdsad")
    suspend fun getNotes(@Header("Authorization") token: String, @Body taskId: TaskById) : Response<List<NoteDataResponse>>


    //группы
    @POST("api/group/create")
    suspend fun addGroup(@Header("Authorization") token: String, @Body group: GroupData) : Response<GroupResponse>

    @POST("api/group/getPersonal")
    suspend fun getPersonalGroup(@Header("Authorization") token: String): Response<List<GroupResponse>>

    @POST("api/group/getPrivate")
    suspend fun getGroupGroup(@Header("Authorization") token: String): Response<List<GroupResponse>>

    @POST("api/group/getById")
    suspend fun getGroupById(@Header("Authorization") token: String, @Body groupId: GroupById): Response<GroupResponse>

    @POST("api/group/update")
    suspend fun updateGroup(@Header("Authorization") token: String, @Body group: GroupUpdate): Response<GroupResponse>

    @HTTP(method = "DELETE", path = "api/group/delete", hasBody = true)
    suspend fun removeGroup(@Header("Authorization") token: String, @Body groupId: GroupById) : Response<Unit>

    @POST("api/group/addProjectInGroup")
    suspend fun addProjectInGroup(@Header("Authorization") token: String, @Body group: ProjectInGroup): Response<ProjectDataResponse>

    @POST("api/group/createInvite")
    suspend fun createInvitetoGroup(@Header("Authorization") token: String, @Body groupId: GroupBy_Id) : Response<GroupInviteResponse>

    @POST("api/group/joinByInvitation")
    suspend fun joinGroupByInvitation(@Header("Authorization") token: String, @Body invite: JoinByInviteProjectData) : Response<MessageData>

}