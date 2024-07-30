package com.coolgirl.majko.Screen.Task

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coolgirl.majko.R
import com.coolgirl.majko.commons.ApiError
import com.coolgirl.majko.commons.ApiExeption
import com.coolgirl.majko.commons.ApiSuccess
import com.coolgirl.majko.data.dataStore.UserDataStore
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectById
import com.coolgirl.majko.data.remote.dto.TaskData.TaskById
import com.coolgirl.majko.data.remote.dto.TaskData.TaskByIdUnderscore
import com.coolgirl.majko.data.remote.dto.TaskData.TaskDataResponse
import com.coolgirl.majko.data.remote.dto.TaskData.TaskUpdateData
import com.coolgirl.majko.data.repository.MajkoInfoRepository
import com.coolgirl.majko.data.repository.MajkoTaskRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class TaskViewModel(private val majkoRepository: MajkoTaskRepository,
                    private val majkoInfoRepository: MajkoInfoRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    fun updateSearchString(newSearchString: String) {
        _uiState.update { currentState ->
            val filteredAllTasks = currentState.allTaskList?.filter { task ->
                task.title?.contains(newSearchString, ignoreCase = true) == true ||
                        task.text?.contains(newSearchString, ignoreCase = true) == true
            }

            val filteredFavoritesTasks = currentState.favoritesTaskList?.filter { task ->
                task.title?.contains(newSearchString, ignoreCase = true) == true ||
                        task.text?.contains(newSearchString, ignoreCase = true) == true
            }

            currentState.copy(
                searchString = newSearchString,
                searchAllTaskList = filteredAllTasks,
                searchFavoritesTaskList = filteredFavoritesTasks
            )
        }
    }

    fun updateSearchString(newSearchString: String, whatFilter: Int) {
        when (whatFilter) {
            0 -> { updateEachTask(newSearchString) }
            1 -> { updateFavTask(newSearchString) }
            else -> { updateAllTask(newSearchString) }
        }
    }

    fun updateEachTask(newSearchString: String) {
        _uiState.update { currentState ->
            val filteredAllTasks = currentState.allTaskList?.filter { task ->
                task.title?.contains(newSearchString, ignoreCase = true) == true ||
                        task.text?.contains(newSearchString, ignoreCase = true) == true
            }

            currentState.copy(
                searchString = newSearchString,
                searchAllTaskList = filteredAllTasks,
                searchFavoritesTaskList = null
            )
        }
    }

    fun updateFavTask(newSearchString: String) {
        _uiState.update { currentState ->
            val filteredFavoritesTasks = currentState.favoritesTaskList?.filter { task ->
                task.title?.contains(newSearchString, ignoreCase = true) == true ||
                        task.text?.contains(newSearchString, ignoreCase = true) == true
            }

            currentState.copy(
                searchString = newSearchString,
                searchAllTaskList = null,
                searchFavoritesTaskList = filteredFavoritesTasks
            )
        }
    }

    fun updateAllTask(newSearchString: String) {
        _uiState.update { currentState ->
            val filteredAllTasks = currentState.allTaskList?.filter { task ->
                task.title?.contains(newSearchString, ignoreCase = true) == true ||
                        task.text?.contains(newSearchString, ignoreCase = true) == true
            }

            val filteredFavoritesTasks = currentState.favoritesTaskList?.filter { task ->
                task.title?.contains(newSearchString, ignoreCase = true) == true ||
                        task.text?.contains(newSearchString, ignoreCase = true) == true
            }

            currentState.copy(
                searchString = newSearchString,
                searchAllTaskList = filteredAllTasks,
                searchFavoritesTaskList = filteredFavoritesTasks
            )
        }
    }

    fun openPanel(id: String) {
        val idLength = 36
        val currentIds = uiState.value.longtapTaskId.chunked(idLength)
        val updatedIds = if (currentIds.contains(id)) {
            currentIds.filter { it != id }
        } else {
            currentIds + id
        }.joinToString("")

        _uiState.update { it.copy(isLongtap = updatedIds.isNotEmpty(), longtapTaskId = updatedIds) }
    }

    fun isError(message: Int?) {
        if (uiState.value.isError) {
            _uiState.update { it.copy(isError = false) }
        } else {
            _uiState.update { it.copy(errorMessage = message) }
            _uiState.update { it.copy(isError = true) }
        }
    }

    fun isMessage(message: Int?) {
        if (uiState.value.isMessage) {
            _uiState.update { it.copy(isMessage = false) }
        } else {
            _uiState.update { it.copy(message = message) }
            _uiState.update { it.copy(isMessage = true) }
        }
    }

    fun getPriority(priorityId: Int): Int {
        return when (priorityId) {
            1 -> R.color.green
            2 -> R.color.yellow
            3 -> R.color.orange
            4 -> R.color.red
            else -> R.color.white
        }
    }

    fun getStatus(statusId: Int): String {
        if (!uiState.value.statuses.isNullOrEmpty()) {
            for (item in uiState.value.statuses!!) {
                if (item.id == statusId) {
                    return item.name
                }
            }
        }
        return "Нет"
    }

    fun loadData() {
        loadFavTask()
        loadEachTask()
        loadStatuses()
    }

    fun loadFavTask() {
        viewModelScope.launch {
            majkoRepository.getAllFavorites().collect() { response ->
                when (response) {
                    is ApiSuccess -> {
                        _uiState.update { it.copy(favoritesTaskList = response.data.sortedBy { it.status }) }
                        _uiState.update { it.copy(searchFavoritesTaskList = response.data.sortedBy { it.status }) }
                    }
                    is ApiError -> {
                        Log.d("TAG", "error message = " + response.message)
                    }
                    is ApiExeption -> {
                        Log.d("TAG", "exeption e = " + response.e)
                    }
                }
            }
        }
    }

    fun loadEachTask() {
        viewModelScope.launch {
            majkoRepository.getAllUserTask().collect() { response ->
                when (response) {
                    is ApiSuccess -> {
                        val notFavorite: MutableList<TaskDataResponse> = mutableListOf()
                        response.data?.forEach { item ->
                            if (!item.isFavorite && item.mainTaskId == null) {
                                notFavorite.add(item)
                            }
                        }
                        _uiState.update { it.copy(allTaskList = notFavorite.sortedBy { it.status }) }
                        _uiState.update { it.copy(searchAllTaskList = notFavorite.sortedBy { it.status }) }
                    }
                    is ApiError -> {
                        Log.d("TAG", "error message = " + response.message)
                    }
                    is ApiExeption -> {
                        Log.d("TAG", "exeption e = " + response.e)
                    }
                }
            }
        }
    }

    fun loadStatuses() {
        viewModelScope.launch {
            majkoInfoRepository.getStatuses().collect() { response ->
                when (response) {
                    is ApiSuccess -> {
                        _uiState.update { it.copy(statuses = response.data!!) }
                    }
                    is ApiError -> {
                        Log.d("TAG", "error message = " + response.message)
                    }
                    is ApiExeption -> {
                        Log.d("TAG", "exeption e = " + response.e)
                    }
                }
            }
        }
    }

    fun addFavotite(task_id: String) {
        viewModelScope.launch {
            majkoRepository.addToFavorite(TaskById(task_id)).collect() { response ->
                when (response) {
                    is ApiSuccess -> {
                        loadData()
                    }
                    is ApiError -> {
                        Log.d("TAG", "error message = " + response.message)
                    }
                    is ApiExeption -> {
                        Log.d("TAG", "exeption e = " + response.e)
                    }
                }
            }
        }
    }

    fun removeFavotite(task_id: String) {
        viewModelScope.launch {
            majkoRepository.removeFavotire(TaskById(task_id)).collect() { response ->
                when (response) {
                    is ApiSuccess -> {
                        loadData()
                    }
                    is ApiError -> {
                        Log.d("TAG", "error message = " + response.message)
                    }
                    is ApiExeption -> {
                        Log.d("TAG", "exeption e = " + response.e)
                    }
                }
            }
        }
    }

    fun removeTask() {
        val projectIds = uiState.value.longtapTaskId.chunked(36)
        projectIds.mapNotNull { id ->
            val task = uiState.value.allTaskList?.find { it.id == id }

            task?.let {
                val removeTask = TaskByIdUnderscore(it.id)

                viewModelScope.launch {
                    majkoRepository.removeTask(removeTask).collect { response ->
                        when (response) {
                            is ApiSuccess -> {
                                isMessage(R.string.message_remove_task)
                                loadData()
                            }
                            is ApiError -> {
                                Log.d("TAG", "error message = " + response.message)
                            }
                            is ApiExeption -> {
                                Log.d("TAG", "exeption e = " + response.e)
                            }
                        }
                    }
                }
            }
        }

        _uiState.update { it.copy(longtapTaskId = "", isLongtap = false) }
        loadData()
    }

    fun updateStatus() {
        val projectIds = uiState.value.longtapTaskId.chunked(36)
        projectIds.mapNotNull { id ->
            val task = uiState.value.allTaskList?.find { it.id == id }

            task?.let {
                val updateTask = TaskUpdateData(
                    taskId = it.id,
                    title = it.title!!,
                    text = it.text!!,
                    priorityId = it.priority,
                    deadline = it.deadline,
                    statusId = 5
                )

                viewModelScope.launch {
                    majkoRepository.updateTask(updateTask).collect { response ->
                        when (response) {
                            is ApiSuccess -> {
                                isMessage(R.string.message_update_task)
                                loadData()
                            }
                            is ApiError -> {
                                Log.d("TAG", "error message = " + response.message)
                            }
                            is ApiExeption -> {
                                Log.d("TAG", "exeption e = " + response.e)
                            }
                        }
                    }
                }
            }
        }

        _uiState.update { it.copy(longtapTaskId = "", isLongtap = false) }
        loadData()
    }
}
