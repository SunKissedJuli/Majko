package com.coolgirl.majko.data.remote.dto.NoteData

import com.google.gson.annotations.SerializedName

data class NoteById(
    @SerializedName("note_id") val noteId: String
)
