package com.coolgirl.majko.data.remote

sealed interface ApiResult<T:Any>
class ApiSuccess<T:Any>(val data: T): ApiResult<T>
class ApiError<T:Any>(val code: Int, val message: String?): ApiResult<T>
class ApiExeption<T:Any>(val e: Throwable): ApiResult<T>

fun <T : Any, R : Any> ApiResult<T>.map(transform: (T) -> R): ApiResult<R> {
    return when (this) {
        is ApiSuccess -> ApiSuccess(transform(data))
        is ApiError -> ApiError(code, message)
        is ApiExeption -> ApiExeption(e)
    }
}

fun <T : Any, R : Any> ApiResult<List<T>>.mapList(transform: (T) -> R): ApiResult<List<R>> {
    return when (this) {
        is ApiSuccess -> ApiSuccess(data.map(transform))
        is ApiError -> ApiError(code, message)
        is ApiExeption -> ApiExeption(e)
    }
}