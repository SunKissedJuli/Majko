package com.coolgirl.majko.data.dataUi.User

import com.coolgirl.majko.data.remote.dto.User.CurrentUserDataResponse

data class CurrentUserDataResponseUi(
    var id : String,
    var createdAt : String,
    var updatedAt : String,
    var name : String,
    var image : String,
    var email : String,
){
    companion object{
        fun empty() = CurrentUserDataResponseUi(
            id = "",
            createdAt = "",
            updatedAt = "",
            name = "",
            image = "",
            email = "")
    }
}

fun CurrentUserDataResponse.toUi() : CurrentUserDataResponseUi{
   return CurrentUserDataResponseUi(
        id = this.id.orEmpty(),
        createdAt = this.createdAt.orEmpty(),
        updatedAt = this.updatedAt.orEmpty(),
        name = this.name.orEmpty(),
        image = this.image.orEmpty(),
        email = this.email.orEmpty(),
    )
}
