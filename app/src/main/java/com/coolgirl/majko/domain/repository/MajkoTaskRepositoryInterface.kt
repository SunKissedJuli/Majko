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

    fun getAllUserTask(): Flow<ApiResult<List<TaskDataResponse>>>

    fun postNewTask(task: TaskData): Flow<ApiResult<TaskDataResponse>>

    fun getTaskById(taskId: TaskById): Flow<ApiResult<TaskDataResponse>>

    fun removeTask(taskId: TaskBy_Id): Flow<ApiResult<Unit>>

    fun updateTask(taskData: TaskUpdateData): Flow<ApiResult<TaskDataResponse>>

    fun getSubtask(taskId: TaskById): Flow<ApiResult<List<TaskDataResponse>>>

    //фавориты
    fun removeFavotire(taskId: TaskById): Flow<ApiResult<MessageData>>

    fun addToFavorite(taskId: TaskById): Flow<ApiResult<MessageData>>

    fun getAllFavorites(): Flow<ApiResult<List<TaskDataResponse>>>

    //записи
    fun addNote(note: NoteData) : Flow<ApiResult<NoteDataResponse>>

    fun updateNote(note: NoteUpdate) : Flow<ApiResult<NoteDataResponse>>

    fun removeNote(noteId: NoteById) : Flow<ApiResult<Unit>>

    fun getNotes(taskId: TaskById) : Flow<ApiResult<List<NoteDataResponse>>>
}