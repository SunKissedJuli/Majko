package com.coolgirl.majko.data

import com.coolgirl.majko.data.dataUi.User.CurrentUserDataResponseUi
import com.coolgirl.majko.data.dataUi.User.UserSignInDataResponseUi
import com.coolgirl.majko.data.dataUi.User.UserSignUpDataResponseUi
import com.coolgirl.majko.data.dataUi.User.toUi
import com.coolgirl.majko.data.remote.ApiMajko
import com.coolgirl.majko.data.remote.ApiResult
import com.coolgirl.majko.data.remote.dto.*
import com.coolgirl.majko.data.remote.dto.UserSignUpData.UserSignUpData
import com.coolgirl.majko.data.remote.handler
import com.coolgirl.majko.data.remote.map
import com.coolgirl.majko.domain.repository.MajkoUserRepositoryInterface
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

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

    override fun updateUserImage(user: UserUpdateImage, file: File): Flow<ApiResult<CurrentUserDataResponseUi>> = flow {
        val namePart = RequestBody.create("text/plain".toMediaTypeOrNull(), user.name ?: "")
        val filePart = MultipartBody.Part.createFormData(
            "image",
            file.name,
            file.asRequestBody("image/*".toMediaTypeOrNull())
        )
        val apiResult = handler { api.updateUserImage(namePart, filePart) }
        val uiResult = apiResult.map { it.toUi() }
        emit(uiResult)
    }

}

