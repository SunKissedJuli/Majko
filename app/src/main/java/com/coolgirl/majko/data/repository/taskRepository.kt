package com.coolgirl.majko.data.repository

import com.coolgirl.majko.data.remote.ApiMajko
import com.coolgirl.majko.data.remote.ApiResult
import com.coolgirl.majko.data.remote.dto.MessageData
import com.coolgirl.majko.data.remote.dto.NoteData.NoteById
import com.coolgirl.majko.data.remote.dto.NoteData.NoteData
import com.coolgirl.majko.data.remote.dto.NoteData.NoteDataResponse
import com.coolgirl.majko.data.remote.dto.NoteData.NoteUpdate
import com.coolgirl.majko.data.remote.dto.TaskData.*
import com.coolgirl.majko.data.remote.handler
import com.coolgirl.majko.domain.repository.MajkoTaskRepositoryInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MajkoTaskRepository @Inject constructor(
    private val api: ApiMajko
): MajkoTaskRepositoryInterface {

    //таски
    override fun getAllUserTask(search: SearchTask): Flow<ApiResult<List<TaskDataResponse>>> = flow {
        emit(handler { api.getAllUserTask(search) })
    }

    override fun postNewTask(task: TaskData): Flow<ApiResult<TaskDataResponse>> = flow {
        emit(handler { api.postNewTask(task) })
    }

    override fun getTaskById(taskId: TaskById): Flow<ApiResult<TaskDataResponse>> = flow {
        emit(handler { api.getTaskById(taskId) })
    }

    override fun updateTask(taskData: TaskUpdateData) : Flow<ApiResult<TaskDataResponse>> = flow {
        emit(handler { api.updateTask(taskData) })
    }

    override fun removeTask(taskId: TaskByIdUnderscore): Flow<ApiResult<Unit>> = flow {
        emit(handler { api.removeTask(taskId) })
    }

    override fun getSubtask(taskId: TaskById): Flow<ApiResult<List<TaskDataResponse>>> = flow {
        emit(handler { api.getSubtask(taskId) })
    }

    //фавориты
    override fun removeFavotire(taskId: TaskById): Flow<ApiResult<MessageData>> = flow {
        emit(handler { api.removeFavotire(taskId) })
    }

    override fun addToFavorite(taskId: TaskById): Flow<ApiResult<MessageData>> = flow {
        emit(handler { api.addToFavorite(taskId) })
    }

    override fun getAllFavorites(): Flow<ApiResult<List<TaskDataResponse>>> = flow {
        emit(handler { api.getAllFavorites()})
    }

    //записи
    override fun addNote(note: NoteData): Flow<ApiResult<NoteDataResponse>> = flow {
        emit(handler { api.addNote(note)})
    }

    override fun updateNote(note: NoteUpdate): Flow<ApiResult<NoteDataResponse>> = flow {
        emit(handler { api.updateNote(note)})
    }

    override fun removeNote(noteId: NoteById): Flow<ApiResult<Unit>> = flow {
        emit(handler { api.removeNote(noteId)})
    }

    override fun getNotes(taskId: TaskById): Flow<ApiResult<List<NoteDataResponse>>> = flow {
        emit(handler { api.getNotes(taskId) })
    }


}