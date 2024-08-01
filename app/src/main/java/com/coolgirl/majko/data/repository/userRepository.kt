package com.coolgirl.majko.data

import com.coolgirl.majko.data.remote.ApiMajko
import com.coolgirl.majko.data.remote.ApiResult
import com.coolgirl.majko.data.remote.dto.*
import com.coolgirl.majko.data.remote.dto.User.CurrentUserDataResponse
import com.coolgirl.majko.data.remote.dto.UserSignUpData.UserSignUpData
import com.coolgirl.majko.data.remote.dto.UserSignUpData.UserSignUpDataResponse
import com.coolgirl.majko.data.remote.handler
import com.coolgirl.majko.domain.repository.MajkoUserRepositoryInterface
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MajkoUserRepository @Inject constructor(
    private val api: ApiMajko
): MajkoUserRepositoryInterface {

    override fun signIn(user: UserSignInData?): Flow<ApiResult<UserSignInDataResponse>> = flow {
        emit(handler { api.signIn(user) })
    }

    override fun currentUser(): Flow<ApiResult<CurrentUserDataResponse>> = flow {
        emit(handler { api.currentUser() })
    }

    override fun signUp(user: UserSignUpData?): Flow<ApiResult<UserSignUpDataResponse>> = flow {
        emit(handler { api.signUp(user) })
    }

    override fun updateUserName(
        user: UserUpdateName
    ): Flow<ApiResult<CurrentUserDataResponse>> = flow {
        emit(handler { api.updateUserName(user) })
    }

    override fun updateUserEmail(
        user: UserUpdateEmail
    ): Flow<ApiResult<CurrentUserDataResponse>> = flow {
        emit(handler { api.updateUserEmail(user) })
    }

    override fun updateUserPassword(
        user: UserUpdatePassword
    ): Flow<ApiResult<CurrentUserDataResponse>> = flow {
        emit(handler { api.updateUserPassword(user) })
    }

    override fun updateUserImage(
        user: UserUpdateImage
    ): Flow<ApiResult<CurrentUserDataResponse>> = flow {
        emit(handler { api.updateUserImage(user) })
    }

}

