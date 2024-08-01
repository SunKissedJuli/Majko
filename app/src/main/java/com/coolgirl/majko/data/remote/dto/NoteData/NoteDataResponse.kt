package com.coolgirl.majko.data.remote.dto.NoteData

import com.coolgirl.majko.data.remote.dto.User.CurrentUserDataResponse
import com.google.gson.annotations.SerializedName

data class NoteDataResponse(
    @SerializedName("id") val id: String,
    @SerializedName("note") val note: String,
    @SerializedName("author") val author: CurrentUserDataResponse,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("isPersonal") val isPersonal: Boolean,
    @SerializedName("files") val files: List<String>,
    @SerializedName("countFiles") val countFiles: Int
)
