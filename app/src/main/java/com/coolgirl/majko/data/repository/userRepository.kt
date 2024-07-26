package com.coolgirl.majko.data

import com.coolgirl.majko.commons.ApiResult
import com.coolgirl.majko.data.remote.ApiMajko
import com.coolgirl.majko.data.remote.dto.*
import com.coolgirl.majko.data.remote.dto.GroupData.*
import com.coolgirl.majko.data.remote.dto.Info.Info
import com.coolgirl.majko.data.remote.dto.NoteData.NoteById
import com.coolgirl.majko.data.remote.dto.NoteData.NoteData
import com.coolgirl.majko.data.remote.dto.NoteData.NoteDataResponse
import com.coolgirl.majko.data.remote.dto.NoteData.NoteUpdate
import com.coolgirl.majko.data.remote.dto.ProjectData.*
import com.coolgirl.majko.data.remote.dto.TaskData.*
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

    override fun currentUser(token: String): Flow<ApiResult<CurrentUserDataResponse>> = flow {
        emit(handler { api.currentUser(token) })
    }

    override fun signUp(user: UserSignUpData?): Flow<ApiResult<UserSignUpDataResponse>> = flow {
        emit(handler { api.signUp(user) })
    }

    override fun updateUserName(
        token: String,
        user: UserUpdateName
    ): Flow<ApiResult<CurrentUserDataResponse>> = flow {
        emit(handler { api.updateUserName(token, user) })
    }

    override fun updateUserEmail(
        token: String,
        user: UserUpdateEmail
    ): Flow<ApiResult<CurrentUserDataResponse>> = flow {
        emit(handler { api.updateUserEmail(token, user) })
    }

    override fun updateUserPassword(
        token: String,
        user: UserUpdatePassword
    ): Flow<ApiResult<CurrentUserDataResponse>> = flow {
        emit(handler { api.updateUserPassword(token, user) })
    }

    override fun updateUserImage(
        token: String,
        user: UserUpdateImage
    ): Flow<ApiResult<CurrentUserDataResponse>> = flow {
        emit(handler { api.updateUserImage(token, user) })
    }

}

