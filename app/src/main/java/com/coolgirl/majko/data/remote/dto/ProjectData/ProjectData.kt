package com.coolgirl.majko.data.remote.dto.ProjectData

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName

data class ProjectData(
    @SerializedName("name") val name : String = "",
    @SerializedName("description") val description : String = "",
    @SerializedName("is_archive") val isArchive : Int = 0
)
