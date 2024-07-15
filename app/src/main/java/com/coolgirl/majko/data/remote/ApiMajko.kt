package com.coolgirl.majko.data.remote

import com.coolgirl.majko.data.remote.dto.CurrentUserDataResponse
import com.coolgirl.majko.data.remote.dto.FavoritesDataResponse
import com.coolgirl.majko.data.remote.dto.TaskData.TaskData
import com.coolgirl.majko.data.remote.dto.TaskData.TaskDataResponse
import com.coolgirl.majko.data.remote.dto.UserSignInData
import com.coolgirl.majko.data.remote.dto.UserSignInDataResponse
import com.coolgirl.majko.data.remote.dto.UserSignUpData.UserSignUpData
import com.coolgirl.majko.data.remote.dto.UserSignUpData.UserSignUpDataResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiMajko{
    @POST("api/auth/local/signin")
    fun signIn(@Body user: UserSignInData?): Call<UserSignInDataResponse>

    @POST("api/auth/local/signup")
    fun signUp(@Body user: UserSignUpData?): Call<UserSignUpDataResponse>

    @GET("api/user/current")
    fun currentUser(@Header("Authorization") tocken: String): Call<CurrentUserDataResponse>

    @POST("api/task/allUserTasks")
    fun getAllUserTask(@Header("Authorization") tocken: String): Call<List<TaskDataResponse>>

    @POST("api/task/create")
    fun postNewTask(@Header("Authorization") tocken: String, @Body task:TaskData): Call<TaskDataResponse>

    @GET("api/task/favorites/")
    fun getAllFavorites(@Header("Authorization") tocken: String): Call<FavoritesDataResponse>

}