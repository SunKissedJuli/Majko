package com.coolgirl.majko.data.remote

import com.coolgirl.majko.data.remote.dto.UserSignInData
import com.coolgirl.majko.data.remote.dto.UserSignInDataResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiMajko{
    @POST("api/auth/local/signin")
    fun signIn(@Body user: UserSignInData?): Call<UserSignInDataResponse>
}