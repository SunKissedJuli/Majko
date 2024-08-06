package com.coolgirl.majko.data.dataUi.ProjectData

import com.coolgirl.majko.data.dataUi.TaskData.TaskDataResponseUi
import com.coolgirl.majko.data.dataUi.TaskData.toUi
import com.coolgirl.majko.data.dataUi.User.CurrentUserDataResponseUi
import com.coolgirl.majko.data.dataUi.User.toUi
import com.coolgirl.majko.data.remote.dto.ProjectData.*

data class ProjectCurrentResponseUi(
    val id: String,
    val createdAt: String,
    val updatedAt: String,
    val name: String,
    val description: String,
    val isArchive: Int,
    val author: CurrentUserDataResponseUi,
    val members: List<MemberUi>,
    val image: String?,
    val isPersonal: Boolean,
    val countFiles: Int,
    val tasks: List<TaskDataResponseUi>,
    val groups: List<GroupUi>,
    val files: List<FileUi>
){
    companion object {
        fun empty() = ProjectCurrentResponseUi(
            id = "",
            createdAt = "",
            updatedAt = "",
            name = "",
            description = "",
            isArchive = 0,
            author = CurrentUserDataResponseUi.empty(),
            members = emptyList(),
            image = null,
            isPersonal = false,
            countFiles = 0,
            tasks = emptyList(),
            groups = emptyList(),
            files = emptyList()
        )
    }
}

fun ProjectCurrentResponse.toUi(): ProjectCurrentResponseUi {
    return ProjectCurrentResponseUi(
        id = this.id.orEmpty(),
        createdAt = this.createdAt.orEmpty(),
        updatedAt = this.updatedAt.orEmpty(),
        name = this.name.orEmpty(),
        description = this.description.orEmpty(),
        isArchive = this.isArchive ?: 0,
        author = this.author?.toUi() ?: CurrentUserDataResponseUi.empty(),
        members = this.members?.map { it.toUi() } ?: emptyList(),
        image = this.image,
        isPersonal = this.isPersonal ?: false,
        countFiles = this.countFiles ?: 0,
        tasks = this.tasks?.map { it.toUi() } ?: emptyList(),
        groups = this.groups?.map { it.toUi() } ?: emptyList(),
        files = this.files?.map { it.toUi() } ?: emptyList()
    )
}

data class GroupUi(
    val id: String,
    val name: String
)

fun Group.toUi(): GroupUi {
    return GroupUi(
        id = this.id.orEmpty(),
        name = this.name.orEmpty()
    )
}

data class FileUi(
    val id: String,
    val name: String,
    val url: String
)

fun File.toUi(): FileUi {
    return FileUi(
        id = this.id.orEmpty(),
        name = this.name.orEmpty(),
        url = this.url.orEmpty()
    )
}


data class MemberUi(
    val projectMemberId: String,
    val user: CurrentUserDataResponseUi,
    val roleId: ProjectRoleUi
)

fun Member.toUi(): MemberUi {
    return MemberUi(
        projectMemberId = this.projectMemberId.orEmpty(),
        user = this.user?.toUi() ?: CurrentUserDataResponseUi.empty(),
        roleId = this.roleId?.toUi() ?: ProjectRoleUi.empty()
    )
}

data class ProjectRoleUi(
    val id: Int,
    val name: String,
    val createdAt: String,
    val updatedAt: String,
){
    companion object{
        fun empty() = ProjectRoleUi(
            id = 0,
            name = "",
            createdAt = "",
            updatedAt = ""
        )
    }
}

fun ProjectRole.toUi(): ProjectRoleUi {
    return ProjectRoleUi(
        id = this.id?:0,
        name = this.name.orEmpty(),
        createdAt = this.createdAt.orEmpty(),
        updatedAt = this.updatedAt.orEmpty(),
    )
}



