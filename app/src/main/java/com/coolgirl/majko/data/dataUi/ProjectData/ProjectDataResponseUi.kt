package com.coolgirl.majko.data.dataUi.ProjectData

import androidx.compose.ui.res.stringResource
import com.coolgirl.majko.R
import com.coolgirl.majko.data.dataUi.User.CurrentUserDataResponseUi
import com.coolgirl.majko.data.dataUi.User.toUi
import com.coolgirl.majko.data.remote.dto.ProjectData.Member
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectDataResponse
import com.coolgirl.majko.data.remote.dto.User.CurrentUserDataResponse
import com.google.gson.annotations.SerializedName

data class ProjectDataResponseUi(
    val id: String,
    val createdAt: String,
    val updatedAt: String,
    val name: String,
    val description: String,
    var isArchive: Int,
    val author: CurrentUserDataResponseUi,
    val members: List<Member>,
    val image: String,
    var isPersonal: Boolean,
    val countFiles: Int,
){
    companion object{
        fun empty() = ProjectDataResponseUi(
            id = "",
            createdAt = "",
            updatedAt = "",
            name = "",
            description = "",
            isArchive = 0,
            author = CurrentUserDataResponseUi.empty(),
            members = listOf(),
            image = "",
            isPersonal = true,
            countFiles = 0
        )
    }
}

fun ProjectDataResponse.toUi() : ProjectDataResponseUi{
    return ProjectDataResponseUi(
        id = this.id.orEmpty(),
        createdAt = this.createdAt.orEmpty(),
        updatedAt = this.updatedAt.orEmpty(),
        name = this.name.orEmpty(),
        description = this.description.orEmpty(),
        isArchive = this.isArchive?:0,
        author = this.author?.toUi()?: CurrentUserDataResponseUi.empty(),
        members = this.members?: emptyList(),
        image = this.image.orEmpty(),
        isPersonal = this.isPersonal?:true,
        countFiles = this.countFiles?:0
    )
}
