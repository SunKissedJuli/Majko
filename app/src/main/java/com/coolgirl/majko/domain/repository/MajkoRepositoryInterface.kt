package com.coolgirl.majko.domain.repository

import com.coolgirl.majko.commons.ApiResult
import com.coolgirl.majko.data.remote.dto.CurrentUserDataResponse
import com.coolgirl.majko.data.remote.dto.UserSignInData
import com.coolgirl.majko.data.remote.dto.UserSignInDataResponse
import com.coolgirl.majko.data.remote.dto.UserSignUpData.UserSignUpData
import com.coolgirl.majko.data.remote.dto.UserSignUpData.UserSignUpDataResponse
import kotlinx.coroutines.flow.Flow

/*interface MajkoRepositoryInterface {

    fun signIn(user: UserSignInData?): Flow<ApiResult<UserSignInDataResponse>>

    fun signUp(user: UserSignUpData?): Flow<ApiResult<UserSignUpDataResponse>>

    fun currentUser(token: String): Flow<ApiResult<CurrentUserDataResponse>>

}*/