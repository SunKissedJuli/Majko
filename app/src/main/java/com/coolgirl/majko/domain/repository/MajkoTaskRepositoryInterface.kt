package com.coolgirl.majko.domain.repository

import com.coolgirl.majko.data.dataUi.MessageDataUi
import com.coolgirl.majko.data.dataUi.NoteData.NoteDataResponseUi
import com.coolgirl.majko.data.dataUi.TaskData.TaskDataResponseUi
import com.coolgirl.majko.data.remote.ApiResult
import com.coolgirl.majko.data.remote.dto.MessageData
import com.coolgirl.majko.data.remote.dto.NoteData.NoteById
import com.coolgirl.majko.data.remote.dto.NoteData.NoteData
import com.coolgirl.majko.data.remote.dto.NoteData.NoteDataResponse
import com.coolgirl.majko.data.remote.dto.NoteData.NoteUpdate
import com.coolgirl.majko.data.remote.dto.TaskData.*
import kotlinx.coroutines.flow.Flow

interface MajkoTaskRepositoryInterface {

    fun getAllUserTask(search: SearchTask): Flow<ApiResult<List<TaskDataResponseUi>>>

    fun postNewTask(task: TaskData): Flow<ApiResult<TaskDataResponseUi>>

    fun getTaskById(taskId: TaskById): Flow<ApiResult<TaskDataResponseUi>>

    fun removeTask(taskId: TaskByIdUnderscore): Flow<ApiResult<Unit>>

    fun updateTask(taskData: TaskUpdateData): Flow<ApiResult<TaskDataResponseUi>>

    fun getSubtask(taskId: TaskById): Flow<ApiResult<List<TaskDataResponseUi>>>

    //фавориты
    fun removeFavotire(taskId: TaskById): Flow<ApiResult<MessageDataUi>>

    fun addToFavorite(taskId: TaskById): Flow<ApiResult<MessageDataUi>>

    fun getAllFavorites(): Flow<ApiResult<List<TaskDataResponseUi>>>

    //записи
    fun addNote(note: NoteData) : Flow<ApiResult<NoteDataResponseUi>>

    fun updateNote(note: NoteUpdate) : Flow<ApiResult<NoteDataResponseUi>>

    fun removeNote(noteId: NoteById) : Flow<ApiResult<Unit>>

    fun getNotes(taskId: TaskById) : Flow<ApiResult<List<NoteDataResponseUi>>>
}