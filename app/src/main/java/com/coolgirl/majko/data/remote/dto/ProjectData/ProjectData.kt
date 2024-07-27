package com.coolgirl.majko.data.remote.dto.ProjectData

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName

data class ProjectData(
    val name : String = "",
    val description : String = "",
    @SerializedName("is_archive") val isArchive : Int = 0
)
