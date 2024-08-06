package com.coolgirl.majko.data

import com.coolgirl.majko.data.dataUi.User.CurrentUserDataResponseUi
import com.coolgirl.majko.data.dataUi.User.UserSignInDataResponseUi
import com.coolgirl.majko.data.dataUi.User.UserSignUpDataResponseUi
import com.coolgirl.majko.data.dataUi.User.toUi
import com.coolgirl.majko.data.remote.ApiMajko
import com.coolgirl.majko.data.remote.ApiResult
import com.coolgirl.majko.data.remote.dto.*
import com.coolgirl.majko.data.remote.dto.User.CurrentUserDataResponse
import com.coolgirl.majko.data.remote.dto.UserSignUpData.UserSignUpData
import com.coolgirl.majko.data.remote.dto.UserSignUpData.UserSignUpDataResponse
import com.coolgirl.majko.data.remote.handler
import com.coolgirl.majko.data.remote.map
import com.coolgirl.majko.domain.repository.MajkoUserRepositoryInterface
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MajkoUserRepository @Inject constructor(
    private val api: ApiMajko
): MajkoUserRepositoryInterface {

    override fun signIn(user: UserSignInData?): Flow<ApiResult<UserSignInDataResponseUi>> = flow {
        val apiResult = handler { api.signIn(user) }
        val uiResult = apiResult.map { it.toUi() }
        emit(uiResult)
    }

    override fun currentUser(): Flow<ApiResult<CurrentUserDataResponseUi>> = flow {
        val apiResult = handler { api.currentUser() }
        val uiResult = apiResult.map { it.toUi() }
        emit(uiResult)
    }

    override fun signUp(user: UserSignUpData?): Flow<ApiResult<UserSignUpDataResponseUi>> = flow {
        val apiResult = handler { api.signUp(user) }
        val uiResult = apiResult.map { it.toUi() }
        emit(uiResult)
    }

    override fun updateUserName(user: UserUpdateName): Flow<ApiResult<CurrentUserDataResponseUi>> = flow {
        val apiResult = handler { api.updateUserName(user) }
        val uiResult = apiResult.map { it.toUi() }
        emit(uiResult)
    }

    override fun updateUserEmail(user: UserUpdateEmail): Flow<ApiResult<CurrentUserDataResponseUi>> = flow {
        val apiResult = handler { api.updateUserEmail(user) }
        val uiResult = apiResult.map { it.toUi() }
        emit(uiResult)
    }

    override fun updateUserPassword(user: UserUpdatePassword): Flow<ApiResult<CurrentUserDataResponseUi>> = flow {
        val apiResult = handler { api.updateUserPassword(user) }
        val uiResult = apiResult.map { it.toUi() }
        emit(uiResult)
    }

    override fun updateUserImage(user: UserUpdateImage): Flow<ApiResult<CurrentUserDataResponseUi>> = flow {
        val apiResult = handler { api.updateUserImage(user) }
        val uiResult = apiResult.map { it.toUi() }
        emit(uiResult)
    }

}

