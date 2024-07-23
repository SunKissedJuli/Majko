package com.coolgirl.majko.data.remote

import com.coolgirl.majko.commons.ApiError
import com.coolgirl.majko.commons.ApiExeption
import com.coolgirl.majko.commons.ApiResult
import com.coolgirl.majko.commons.ApiSuccess
import retrofit2.HttpException
import retrofit2.Response

suspend fun <T:Any> handler(
    execute:suspend ()-> Response<T>
): ApiResult<T> {
    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful && body != null){
            ApiSuccess(body)
        }else{
            ApiError(code = response.code(), message = response.message())
        }
    }catch (e: HttpException){
        ApiError(code = e.code(), message = e.message())
    }catch (e:Throwable){
        ApiExeption(e)
    }
}