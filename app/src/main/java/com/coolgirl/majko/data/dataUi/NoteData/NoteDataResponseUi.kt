package com.coolgirl.majko.data.dataUi.NoteData

import com.coolgirl.majko.data.dataUi.User.CurrentUserDataResponseUi
import com.coolgirl.majko.data.dataUi.User.toUi
import com.coolgirl.majko.data.remote.dto.NoteData.NoteDataResponse

data class NoteDataResponseUi(
    val id: String,
    val note: String,
    val author: CurrentUserDataResponseUi,
    val createdAt: String,
    val updatedAt: String,
    val isPersonal: Boolean,
    val files: List<String>,
    val countFiles: Int
)

fun NoteDataResponse.toUi(): NoteDataResponseUi{
    return NoteDataResponseUi(
        id = this.id.orEmpty(),
        note = this.note.orEmpty(),
        author = this.author?.toUi()?: CurrentUserDataResponseUi.empty(),
        createdAt = this.createdAt.orEmpty(),
        updatedAt = this.updatedAt.orEmpty(),
        isPersonal = this.isPersonal?:true,
        files = this.files?: emptyList(),
        countFiles = this.countFiles?:0
    )
}
