package com.coolgirl.majko.data.remote

sealed interface ApiResult<T:Any>
class ApiSuccess<T:Any>(val data: T): ApiResult<T>
class ApiError<T:Any>(val code: Int, val message: String?): ApiResult<T>
class ApiExeption<T:Any>(val e: Throwable): ApiResult<T>