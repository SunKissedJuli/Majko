package com.coolgirl.majko.data.repository

import com.coolgirl.majko.commons.ApiResult
import com.coolgirl.majko.data.remote.ApiMajko
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
    override fun getAllUserTask(token: String): Flow<ApiResult<List<TaskDataResponse>>> = flow {
        emit(handler { api.getAllUserTask(token) })
    }

    override fun postNewTask(token: String, task: TaskData): Flow<ApiResult<TaskDataResponse>> = flow {
        emit(handler { api.postNewTask(token, task) })
    }

    override fun getTaskById(token: String, taskId: TaskById): Flow<ApiResult<TaskDataResponse>> = flow {
        emit(handler { api.getTaskById(token, taskId) })
    }

    override fun updateTask(token: String, taskData: TaskUpdateData) : Flow<ApiResult<TaskDataResponse>> = flow {
        emit(handler { api.updateTask(token, taskData) })
    }

    override fun removeTask(token: String, taskId: TaskBy_Id): Flow<ApiResult<Unit>> = flow {
        emit(handler { api.removeTask(token, taskId) })
    }

    override fun getSubtask(token: String, taskId: TaskById): Flow<ApiResult<List<TaskDataResponse>>> = flow {
        emit(handler { api.getSubtask(token, taskId) })
    }

    //фавориты
    override fun removeFavotire(token: String, taskId: TaskById): Flow<ApiResult<MessageData>> = flow {
        emit(handler { api.removeFavotire(token, taskId) })
    }

    override fun addToFavorite(token: String, taskId: TaskById): Flow<ApiResult<MessageData>> = flow {
        emit(handler { api.addToFavorite(token, taskId) })
    }

    override fun getAllFavorites(token: String): Flow<ApiResult<List<TaskDataResponse>>> = flow {
        emit(handler { api.getAllFavorites(token)})
    }

    //записи
    override fun addNote(token: String, note: NoteData): Flow<ApiResult<NoteDataResponse>> = flow {
        emit(handler { api.addNote(token, note)})
    }

    override fun updateNote(token: String, note: NoteUpdate): Flow<ApiResult<NoteDataResponse>> = flow {
        emit(handler { api.updateNote(token, note)})
    }

    override fun removeNote(token: String, noteId: NoteById): Flow<ApiResult<Unit>> = flow {
        emit(handler { api.removeNote(token, noteId)})
    }

    override fun getNotes(token: String, taskId: TaskById): Flow<ApiResult<List<NoteDataResponse>>> = flow {
        emit(handler { api.getNotes(token, taskId) })
    }


}