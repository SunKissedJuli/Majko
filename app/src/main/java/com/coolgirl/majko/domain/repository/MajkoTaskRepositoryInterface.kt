package com.coolgirl.majko.domain.repository

import com.coolgirl.majko.commons.ApiResult
import com.coolgirl.majko.data.remote.dto.MessageData
import com.coolgirl.majko.data.remote.dto.NoteData.NoteById
import com.coolgirl.majko.data.remote.dto.NoteData.NoteData
import com.coolgirl.majko.data.remote.dto.NoteData.NoteDataResponse
import com.coolgirl.majko.data.remote.dto.NoteData.NoteUpdate
import com.coolgirl.majko.data.remote.dto.TaskData.*
import kotlinx.coroutines.flow.Flow

interface MajkoTaskRepositoryInterface {

    fun getAllUserTask(token: String): Flow<ApiResult<List<TaskDataResponse>>>

    fun postNewTask(token: String, task: TaskData): Flow<ApiResult<TaskDataResponse>>

    fun getTaskById(token: String, taskId: TaskById): Flow<ApiResult<TaskDataResponse>>

    fun removeTask(token: String, taskId: TaskBy_Id): Flow<ApiResult<Unit>>

    fun updateTask(token: String, taskData: TaskUpdateData): Flow<ApiResult<TaskDataResponse>>

    fun getSubtask(token: String, taskId: TaskById): Flow<ApiResult<List<TaskDataResponse>>>

    //фавориты
    fun removeFavotire(token: String, taskId: TaskById): Flow<ApiResult<MessageData>>

    fun addToFavorite(token: String, taskId: TaskById): Flow<ApiResult<MessageData>>

    fun getAllFavorites(token: String): Flow<ApiResult<List<TaskDataResponse>>>

    //записи
    fun addNote(token: String, note: NoteData) : Flow<ApiResult<NoteDataResponse>>

    fun updateNote(token: String, note: NoteUpdate) : Flow<ApiResult<NoteDataResponse>>

    fun removeNote(token: String, noteId: NoteById) : Flow<ApiResult<Unit>>

    fun getNotes(token: String, taskId: TaskById) : Flow<ApiResult<List<NoteDataResponse>>>
}