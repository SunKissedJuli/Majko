package com.coolgirl.majko.domain.repository

import com.coolgirl.majko.data.dataUi.User.CurrentUserDataResponseUi
import com.coolgirl.majko.data.dataUi.User.UserSignInDataResponseUi
import com.coolgirl.majko.data.dataUi.User.UserSignUpDataResponseUi
import com.coolgirl.majko.data.remote.ApiResult
import com.coolgirl.majko.data.remote.dto.*
import com.coolgirl.majko.data.remote.dto.User.CurrentUserDataResponse
import com.coolgirl.majko.data.remote.dto.UserSignUpData.UserSignUpData
import com.coolgirl.majko.data.remote.dto.UserSignUpData.UserSignUpDataResponse
import kotlinx.coroutines.flow.Flow

interface MajkoUserRepositoryInterface {

   fun signIn(user: UserSignInData?): Flow<ApiResult<UserSignInDataResponseUi>>

   fun currentUser(): Flow<ApiResult<CurrentUserDataResponseUi>>

   fun signUp(user: UserSignUpData?): Flow<ApiResult<UserSignUpDataResponseUi>>

   fun updateUserName(user: UserUpdateName): Flow<ApiResult<CurrentUserDataResponse>>

   fun updateUserEmail(user: UserUpdateEmail): Flow<ApiResult<CurrentUserDataResponse>>

   fun updateUserPassword(user: UserUpdatePassword): Flow<ApiResult<CurrentUserDataResponse>>

   fun updateUserImage(user: UserUpdateImage): Flow<ApiResult<CurrentUserDataResponse>>

}