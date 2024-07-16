package com.coolgirl.majko.data.remote

import com.coolgirl.majko.data.remote.dto.*
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectData
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectDataResponse
import com.coolgirl.majko.data.remote.dto.TaskData.TaskById
import com.coolgirl.majko.data.remote.dto.TaskData.TaskBy_Id
import com.coolgirl.majko.data.remote.dto.TaskData.TaskData
import com.coolgirl.majko.data.remote.dto.TaskData.TaskDataResponse
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

    //таски
    @POST("api/task/allUserTasks")
    fun getAllUserTask(@Header("Authorization") tocken: String): Call<List<TaskDataResponse>>

    @POST("api/task/create")
    fun postNewTask(@Header("Authorization") tocken: String, @Body task:TaskData): Call<TaskDataResponse>

    @POST("api/task/getById")
    fun getTaskById(@Header("Authorization") tocken: String, @Body taskId: TaskById): Call<TaskDataResponse>

    @HTTP(method = "DELETE", path = "api/task/delete", hasBody = true)
    fun removeTask(@Header("Authorization") tocken: String, @Body taskId: TaskBy_Id) : Call<Unit>


    //фавориты
    @HTTP(method = "DELETE", path = "api/task/removeFavorite", hasBody = true)
    fun removeFavotire(@Header("Authorization") tocken: String, @Body taskId: TaskById) : Call<MessageData>

    @POST("api/task/addToFavorite")
    fun addToFavorite(@Header("Authorization") tocken: String, @Body taskId: TaskById) : Call<MessageData>

    @GET("api/task/favorites")
    fun getAllFavorites(@Header("Authorization") tocken: String): Call<List<TaskDataResponse>>


    //проекты
    @POST("api/project/getPersonal")
    fun getPersonalProject(@Header("Authorization") tocken: String): Call<List<ProjectDataResponse>>

    @POST("api/project/getPrivate")
    fun getGroupProject(@Header("Authorization") tocken: String): Call<List<ProjectDataResponse>>

    @POST("api/project/create")
    fun postNewProject(@Header("Authorization") tocken: String, @Body project:ProjectData): Call<ProjectDataResponse>

}