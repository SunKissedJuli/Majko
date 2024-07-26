package com.coolgirl.majko.domain.repository

import com.coolgirl.majko.commons.ApiResult
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
import kotlinx.coroutines.flow.Flow

interface MajkoUserRepositoryInterface {

   fun signIn(user: UserSignInData?): Flow<ApiResult<UserSignInDataResponse>>

   fun currentUser(token: String): Flow<ApiResult<CurrentUserDataResponse>>

   fun signUp(user: UserSignUpData?): Flow<ApiResult<UserSignUpDataResponse>>

   fun updateUserName(token: String, user: UserUpdateName): Flow<ApiResult<CurrentUserDataResponse>>

   fun updateUserEmail(
      token: String,
      user: UserUpdateEmail
   ): Flow<ApiResult<CurrentUserDataResponse>>

   fun updateUserPassword(
      token: String,
      user: UserUpdatePassword
   ): Flow<ApiResult<CurrentUserDataResponse>>

   fun updateUserImage(
      token: String,
      user: UserUpdateImage
   ): Flow<ApiResult<CurrentUserDataResponse>>

}