package com.coolgirl.majko.data.dataUi.TaskData

import com.coolgirl.majko.data.dataUi.ProjectData.ProjectDataResponseUi
import com.coolgirl.majko.data.dataUi.ProjectData.toUi
import com.coolgirl.majko.data.dataUi.User.CurrentUserDataResponseUi
import com.coolgirl.majko.data.dataUi.User.toUi
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectDataResponse
import com.coolgirl.majko.data.remote.dto.TaskData.TaskDataResponse
import com.coolgirl.majko.data.remote.dto.User.CurrentUserDataResponse
import com.google.gson.annotations.SerializedName
import java.util.Optional.empty

data class TaskDataResponseUi(
    val id: String,
    val createdAt: String,
    val updatedAt: String,
    val title: String,
    val text: String,
    val priority: Int,
    val deadline: String,
    val status: Int,
    val image: String,
    val creator: List<CurrentUserDataResponseUi>,
    val mainTaskId: String,
    val taskMembers: List<CurrentUserDataResponseUi>,
    val isPersonal: Boolean,
    val countSubtasks: Int,
    val countNotes: Int,
    val countFiles: Int,
    val isFavorite: Boolean,
    val project: ProjectDataResponseUi
)

fun TaskDataResponse.toUi(): TaskDataResponseUi {
    return TaskDataResponseUi(
        id = this.id.orEmpty(),
        createdAt = this.createdAt.orEmpty(),
        updatedAt = this.updatedAt.orEmpty(),
        title = this.title.orEmpty(),
        text = this.text.orEmpty(),
        priority = this.priority ?: 0,
        deadline = this.deadline.orEmpty(),
        status = this.status ?: 0,
        image = this.image.orEmpty(),
        creator = this.creator?.map { it.toUi() } ?: emptyList(),
        mainTaskId = this.mainTaskId.orEmpty(),
        taskMembers = this.taskMembers?.map { it.toUi() } ?: emptyList(),
        isPersonal = this.isPersonal ?: false,
        countSubtasks = this.countSubtasks ?: 0,
        countNotes = this.countNotes ?: 0,
        countFiles = this.countFiles ?: 0,
        isFavorite = this.isFavorite ?: false,
        project = this.project?.toUi()?: ProjectDataResponseUi.empty()
    )
}

