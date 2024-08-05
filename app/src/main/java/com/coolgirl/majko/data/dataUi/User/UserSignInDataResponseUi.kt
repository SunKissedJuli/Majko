package com.coolgirl.majko.data.dataUi.User

import com.coolgirl.majko.data.remote.dto.UserSignInDataResponse

data class UserSignInDataResponseUi(
    var message: String,
    var status: Int,
    var accessToken: String,
)

fun UserSignInDataResponse.toUi(): UserSignInDataResponseUi{
    return UserSignInDataResponseUi(
        message = this.message.orEmpty(),
        status = this.status?:0,
        accessToken = this.accessToken.orEmpty()
    )
}
