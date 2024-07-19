package com.coolgirl.majko.data.remote

import com.coolgirl.majko.data.remote.dto.*
import com.coolgirl.majko.data.remote.dto.GroupData.GroupData
import com.coolgirl.majko.data.remote.dto.GroupData.GroupResponse
import com.coolgirl.majko.data.remote.dto.NoteData.NoteById
import com.coolgirl.majko.data.remote.dto.NoteData.NoteData
import com.coolgirl.majko.data.remote.dto.NoteData.NoteDataResponse
import com.coolgirl.majko.data.remote.dto.NoteData.NoteUpdate
import com.coolgirl.majko.data.remote.dto.ProjectData.*
import com.coolgirl.majko.data.remote.dto.TaskData.*
import com.coolgirl.majko.data.remote.dto.UserSignUpData.UserSignUpData
import com.coolgirl.majko.data.remote.dto.UserSignUpData.UserSignUpDataResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiMajko{

    //юзер и авторизация
    @POST("api/auth/local/signin")
    fun signIn(@Body user: UserSignInData?): Call<UserSignInDataResponse>

    @POST("api/auth/local/signup")
    fun signUp(@Body user: UserSignUpData?): Call<UserSignUpDataResponse>

    @GET("api/user/current")
    fun currentUser(@Header("Authorization") tocken: String): Call<CurrentUserDataResponse>

    @POST("api/user/update")
    fun updateUserName(@Header("Authorization") tocken: String, @Body user: UserUpdateName): Call<CurrentUserDataResponse>

    @POST("api/user/update")
    fun updateUserEmail(@Header("Authorization") tocken: String, @Body user: UserUpdateEmail): Call<CurrentUserDataResponse>

    @POST("api/user/update")
    fun updateUserPassword(@Header("Authorization") tocken: String, @Body user: UserUpdatePassword): Call<CurrentUserDataResponse>

    //таски
    @POST("api/task/allUserTasks")
    fun getAllUserTask(@Header("Authorization") tocken: String): Call<List<TaskDataResponse>>

    @POST("api/task/create")
    fun postNewTask(@Header("Authorization") tocken: String, @Body task:TaskData): Call<TaskDataResponse>

    @POST("api/task/getById")
    fun getTaskById(@Header("Authorization") tocken: String, @Body taskId: TaskById): Call<TaskDataResponse>

    @HTTP(method = "DELETE", path = "api/task/delete", hasBody = true)
    fun removeTask(@Header("Authorization") tocken: String, @Body taskId: TaskBy_Id) : Call<Unit>

    //субтаски
    @POST("api/task/getSubtaskForTask")
    fun getSubtask(@Header("Authorization") tocken: String, @Body taskId: TaskById): Call<List<TaskDataResponse>>


    //фавориты
    @HTTP(method = "DELETE", path = "api/task/removeFavorite", hasBody = true)
    fun removeFavotire(@Header("Authorization") tocken: String, @Body taskId: TaskById) : Call<MessageData>

    @POST("api/task/addToFavorite")
    fun addToFavorite(@Header("Authorization") tocken: String, @Body taskId: TaskById) : Call<MessageData>

    @GET("api/task/favorites")
    fun getAllFavorites(@Header("Authorization") tocken: String): Call<List<TaskDataResponse>>

    @POST("api/task/update")
    fun updateTask(@Header("Authorization") tocken: String, @Body taskData: TaskUpdateData) : Call<TaskDataResponse>


    //проекты
    @POST("api/project/getPersonal")
    fun getPersonalProject(@Header("Authorization") tocken: String): Call<List<ProjectDataResponse>>

    @POST("api/project/getPrivate")
    fun getGroupProject(@Header("Authorization") tocken: String): Call<List<ProjectDataResponse>>

    @POST("api/project/create")
    fun postNewProject(@Header("Authorization") tocken: String, @Body project:ProjectData): Call<ProjectDataResponse>

    @POST("api/project/getById")
    fun getProjectById(@Header("Authorization") tocken: String, @Body projectById: ProjectById) : Call<ProjectCurrentResponse>

    @POST("api/project/update")
    fun updateProject(@Header("Authorization") tocken: String, @Body projectById: ProjectUpdate) : Call<ProjectCurrentResponse>

    @HTTP(method = "DELETE", path = "api/project/delete", hasBody = true)
    fun removeProject(@Header("Authorization") tocken: String, @Body taskId: ProjectById) : Call<Unit>

    @POST("api/project/createInvite")
    fun createInvitetoProject(@Header("Authorization") tocken: String, @Body projectById: ProjectBy_Id) : Call<ProjectCreateInviteResponse>

    @POST("api/project/joinByInvitation")
    fun joinByInvitation(@Header("Authorization") tocken: String, @Body invite: JoinByInviteProjectData) : Call<MessageData>


    //записи
    @POST("api/note/create")
    fun addNote(@Header("Authorization") tocken: String, @Body note: NoteData) : Call<NoteDataResponse>

    @POST("api/note/update")
    fun updateNote(@Header("Authorization") tocken: String, @Body note: NoteUpdate) : Call<NoteDataResponse>

    @HTTP(method = "DELETE", path = "api/note/delete", hasBody = true)
    fun removeNote(@Header("Authorization") tocken: String, @Body noteId: NoteById) : Call<Unit>

    @POST("api/note/getNotes?aseae=asdsad")
    fun getNotes(@Header("Authorization") tocken: String, @Body taskId: TaskById) : Call<List<NoteDataResponse>>


    //группы
    @POST("api/group/create")
    fun addGroup(@Header("Authorization") tocken: String, @Body group: GroupData) : Call<GroupResponse>

    @POST("api/group/getPersonal")
    fun getPersonalGroup(@Header("Authorization") tocken: String): Call<List<GroupResponse>>

    @POST("api/group/getPrivate")
    fun getGroupGroup(@Header("Authorization") tocken: String): Call<List<GroupResponse>>

}