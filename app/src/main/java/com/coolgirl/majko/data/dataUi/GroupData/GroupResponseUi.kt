package com.coolgirl.majko.data.dataUi.GroupData

import com.coolgirl.majko.data.dataUi.ProjectData.ProjectDataResponseUi
import com.coolgirl.majko.data.dataUi.ProjectData.ProjectRoleUi
import com.coolgirl.majko.data.dataUi.ProjectData.toUi
import com.coolgirl.majko.data.dataUi.User.CurrentUserDataResponseUi
import com.coolgirl.majko.data.dataUi.User.toUi
import com.coolgirl.majko.data.remote.dto.GroupData.GroupMember
import com.coolgirl.majko.data.remote.dto.GroupData.GroupResponse

data class GroupResponseUi(
    val id: String,
    val title: String,
    val description: String,
    val author: CurrentUserDataResponseUi,
    val createdAt: String,
    val updatedAt: String,
    val parentGroup: String,
    val isPersonal: Boolean,
    val image: String?,
    val filesCount: Int,
    val projectsGroup: List<ProjectDataResponseUi>,
    val members: List<GroupMemberUi>,
    val files: List<Any>
){
    companion object {
        fun empty() = GroupResponseUi(
            id = "",
            title = "",
            description = "",
            author = CurrentUserDataResponseUi.empty(),
            createdAt = "",
            updatedAt = "",
            parentGroup = "",
            isPersonal = false,
            image = null,
            filesCount = 0,
            projectsGroup = emptyList(),
            members = emptyList(),
            files = emptyList()
        )
    }
}

data class GroupMemberUi(
    val id: String,
    val user: CurrentUserDataResponseUi,
    val role: ProjectRoleUi
)

fun GroupResponse.toUi(): GroupResponseUi {
    return GroupResponseUi(
        id = this.id.orEmpty(),
        title = this.title.orEmpty(),
        description = this.description.orEmpty(),
        author = this.author?.toUi()?: CurrentUserDataResponseUi.empty(),
        createdAt = this.createdAt.orEmpty(),
        updatedAt = this.updatedAt.orEmpty(),
        parentGroup = this.parentGroup.orEmpty(),
        isPersonal = this.isPersonal?:true,
        image = this.image.orEmpty(),
        filesCount = this.filesCount?:0,
        projectsGroup = this.projectsGroup?.map { it.toUi() }?: emptyList(),
        members = this.members?.map { it.toUi() }?: emptyList(),
        files = this.files?: emptyList()
    )
}

fun GroupMember.toUi(): GroupMemberUi {
    return GroupMemberUi(
        id = this.id.orEmpty(),
        user = this.user?.toUi()?: CurrentUserDataResponseUi.empty(),
        role = this.role?.toUi()?: ProjectRoleUi.empty()
    )
}


