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
import java.io.File

interface MajkoUserRepositoryInterface {

   fun signIn(user: UserSignInData?): Flow<ApiResult<UserSignInDataResponseUi>>

   fun currentUser(): Flow<ApiResult<CurrentUserDataResponseUi>>

   fun signUp(user: UserSignUpData?): Flow<ApiResult<UserSignUpDataResponseUi>>

   fun updateUserName(user: UserUpdateName): Flow<ApiResult<CurrentUserDataResponseUi>>

   fun updateUserEmail(user: UserUpdateEmail): Flow<ApiResult<CurrentUserDataResponseUi>>

   fun updateUserPassword(user: UserUpdatePassword): Flow<ApiResult<CurrentUserDataResponseUi>>

   fun updateUserImage(user: UserUpdateImage, file: File): Flow<ApiResult<CurrentUserDataResponseUi>>

}