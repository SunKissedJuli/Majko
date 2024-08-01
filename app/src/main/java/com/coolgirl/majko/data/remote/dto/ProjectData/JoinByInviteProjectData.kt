package com.coolgirl.majko.data.remote.dto.ProjectData

import com.google.gson.annotations.SerializedName

data class JoinByInviteProjectData(
    @SerializedName("invite") val invite:String
)
