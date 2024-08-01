package com.coolgirl.majko.data.remote.dto.NoteData

import com.google.gson.annotations.SerializedName

data class NoteUpdate(
    @SerializedName("id") val id: String,
    @SerializedName("taskId") val taskId: String,
    @SerializedName("text") val text: String
)
