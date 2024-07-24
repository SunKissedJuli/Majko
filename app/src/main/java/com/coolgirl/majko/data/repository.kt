package com.coolgirl.majko.data

import com.coolgirl.majko.commons.ApiResult
import com.coolgirl.majko.data.remote.ApiMajko
import com.coolgirl.majko.data.remote.dto.CurrentUserDataResponse
import com.coolgirl.majko.data.remote.dto.UserSignInData
import com.coolgirl.majko.data.remote.dto.UserSignInDataResponse
import com.coolgirl.majko.data.remote.dto.UserSignUpData.UserSignUpData
import com.coolgirl.majko.data.remote.dto.UserSignUpData.UserSignUpDataResponse
import com.coolgirl.majko.data.remote.handler
import com.coolgirl.majko.domain.repository.MajkoRepositoryInterface
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/*class MajkoRepository @Inject constructor(
    private val api: ApiMajko
): MajkoRepositoryInterface {

    override fun signIn(user: UserSignInData?): Flow<ApiResult<UserSignInDataResponse>> = flow {
        emit(handler { api.signIn(user) })
    }

    override fun signUp(user: UserSignUpData?): Flow<ApiResult<UserSignUpDataResponse>> = flow {
        emit(handler { api.signUp(user) })
    }

    override fun currentUser(token: String): Flow<ApiResult<CurrentUserDataResponse>> = flow {
        emit(handler { api.currentUser(token) })
    }
}*/
