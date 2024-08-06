package com.coolgirl.majko.data.repository

import com.coolgirl.majko.data.dataUi.Info.toUi
import com.coolgirl.majko.data.dataUi.MessageDataUi
import com.coolgirl.majko.data.dataUi.NoteData.NoteDataResponseUi
import com.coolgirl.majko.data.dataUi.NoteData.toUi
import com.coolgirl.majko.data.dataUi.TaskData.TaskDataResponseUi
import com.coolgirl.majko.data.dataUi.TaskData.toUi
import com.coolgirl.majko.data.dataUi.User.toUi
import com.coolgirl.majko.data.dataUi.toUi
import com.coolgirl.majko.data.remote.*
import com.coolgirl.majko.data.remote.dto.MessageData
import com.coolgirl.majko.data.remote.dto.NoteData.NoteById
import com.coolgirl.majko.data.remote.dto.NoteData.NoteData
import com.coolgirl.majko.data.remote.dto.NoteData.NoteDataResponse
import com.coolgirl.majko.data.remote.dto.NoteData.NoteUpdate
import com.coolgirl.majko.data.remote.dto.TaskData.*
import com.coolgirl.majko.domain.repository.MajkoTaskRepositoryInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MajkoTaskRepository @Inject constructor(
    private val api: ApiMajko
): MajkoTaskRepositoryInterface {

    //таски
    override fun getAllUserTask(search: SearchTask): Flow<ApiResult<List<TaskDataResponseUi>>> = flow {
        val apiResult = handler { api.getAllUserTask(search) }
        val uiResult = apiResult.mapList { it.toUi() }
        emit(uiResult)
    }

    override fun postNewTask(task: TaskData): Flow<ApiResult<TaskDataResponseUi>> = flow {
        val apiResult = handler { api.postNewTask(task) }
        val uiResult = apiResult.map { it.toUi() }
        emit(uiResult)
    }

    override fun getTaskById(taskId: TaskById): Flow<ApiResult<TaskDataResponseUi>> = flow {
        val apiResult = handler { api.getTaskById(taskId) }
        val uiResult = apiResult.map { it.toUi() }
        emit(uiResult)
    }

    override fun updateTask(taskData: TaskUpdateData) : Flow<ApiResult<TaskDataResponseUi>> = flow {
        val apiResult = handler { api.updateTask(taskData) }
        val uiResult = apiResult.map { it.toUi() }
        emit(uiResult)
    }

    override fun removeTask(taskId: TaskByIdUnderscore): Flow<ApiResult<Unit>> = flow {
        emit(handler { api.removeTask(taskId) })
    }

    override fun getSubtask(taskId: TaskById): Flow<ApiResult<List<TaskDataResponseUi>>> = flow {
        val apiResult = handler { api.getSubtask(taskId) }
        val uiResult = apiResult.mapList { it.toUi() }
        emit(uiResult)
    }


    //фавориты
    override fun removeFavotire(taskId: TaskById): Flow<ApiResult<MessageDataUi>> = flow {
        val apiResult = handler { api.removeFavotire(taskId) }
        val uiResult = apiResult.map { it.toUi() }
        emit(uiResult)
    }

    override fun addToFavorite(taskId: TaskById): Flow<ApiResult<MessageDataUi>> = flow {
        val apiResult = handler { api.addToFavorite(taskId) }
        val uiResult = apiResult.map { it.toUi() }
        emit(uiResult)
    }

    override fun getAllFavorites(): Flow<ApiResult<List<TaskDataResponseUi>>> = flow {
        val apiResult = handler { api.getAllFavorites() }
        val uiResult = apiResult.mapList { it.toUi() }
        emit(uiResult)
    }

    //записи
    override fun addNote(note: NoteData): Flow<ApiResult<NoteDataResponseUi>> = flow {
        val apiResult = handler { api.addNote(note) }
        val uiResult = apiResult.map { it.toUi() }
        emit(uiResult)
    }

    override fun updateNote(note: NoteUpdate): Flow<ApiResult<NoteDataResponseUi>> = flow {
        val apiResult = handler { api.updateNote(note) }
        val uiResult = apiResult.map { it.toUi() }
        emit(uiResult)
    }

    override fun removeNote(noteId: NoteById): Flow<ApiResult<Unit>> = flow {
        emit(handler { api.removeNote(noteId)})
    }

    override fun getNotes(taskId: TaskById): Flow<ApiResult<List<NoteDataResponseUi>>> = flow {
        val apiResult = handler { api.getNotes(taskId) }
        val uiResult = apiResult.mapList { it.toUi() }
        emit(uiResult)
    }


}