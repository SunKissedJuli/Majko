package com.coolgirl.majko.data.remote.dto.NoteData

import com.google.gson.annotations.SerializedName

data class NoteData(
    @SerializedName("taskId") val taskId: String,
    @SerializedName("text") val text: String
)
