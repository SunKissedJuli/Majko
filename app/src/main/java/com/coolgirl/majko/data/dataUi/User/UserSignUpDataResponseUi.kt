package com.coolgirl.majko.data.dataUi.User

import com.coolgirl.majko.data.remote.dto.UserSignInDataResponse
import com.coolgirl.majko.data.remote.dto.UserSignUpData.UserSignUpDataResponse

data class UserSignUpDataResponseUi(
    var accessToken: String,
    var refreshToken: String
)

fun UserSignUpDataResponse.toUi(): UserSignUpDataResponseUi{
    return UserSignUpDataResponseUi(
        accessToken = this.accessToken.orEmpty(),
        refreshToken = this.refreshToken.orEmpty()
    )
}