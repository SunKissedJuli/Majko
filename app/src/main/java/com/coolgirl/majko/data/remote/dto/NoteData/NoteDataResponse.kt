package com.coolgirl.majko.data.remote.dto.NoteData

import com.coolgirl.majko.data.remote.dto.CurrentUserDataResponse

data class NoteDataResponse(
    val id: String,
    val note: String,
    val author: CurrentUserDataResponse,
    val createdAt: String,
    val updatedAt: String,
    val isPersonal: Boolean,
    val files: List<String>,
    val countFiles: Int
)
