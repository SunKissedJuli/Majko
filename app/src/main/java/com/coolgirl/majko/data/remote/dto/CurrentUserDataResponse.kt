package com.coolgirl.majko.data.remote.dto

data class CurrentUserDataResponse(
    var id : String,
    var createdAt : String,
    var updatedAt : String,
    var name : String?,
    var image : String?,
    var email : String,
)
