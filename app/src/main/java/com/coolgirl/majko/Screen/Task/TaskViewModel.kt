package com.coolgirl.majko.Screen.Task

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coolgirl.majko.R
import com.coolgirl.majko.data.dataUi.TaskData.TaskDataResponseUi
import com.coolgirl.majko.data.remote.ApiError
import com.coolgirl.majko.data.remote.ApiExeption
import com.coolgirl.majko.data.remote.ApiSuccess
import com.coolgirl.majko.data.remote.dto.TaskData.*
import com.coolgirl.majko.data.repository.MajkoInfoRepository
import com.coolgirl.majko.data.repository.MajkoTaskRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class TaskViewModel(private val majkoRepository: MajkoTaskRepository,
                    private val majkoInfoRepository: MajkoInfoRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(TaskUiState.default())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    fun updateSearchString(newSearchString: String, whatFilter: Int) {
        when (whatFilter) {
            0 -> {
                loadEachTask(newSearchString)
                _uiState.update { it.copy(searchString = newSearchString) } }
            1 -> {
                loadFavTask(newSearchString)
                _uiState.update { it.copy(searchString = newSearchString) } }
            else -> {
                loadAllTask(newSearchString)
                _uiState.update { it.copy(searchString = newSearchString) }}
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
            _uiState.update { it.copy(errorMessage = message, isError = true ) }
        }
    }

    fun isMessage(message: Int?) {
        if (uiState.value.isMessage) {
            _uiState.update { it.copy(isMessage = false) }
        } else {
            _uiState.update { it.copy(message = message, isMessage = true) }
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
        return ""
    }

    fun updateExpandedFilter(){
        if(uiState.value.expandedFilter){
            _uiState.update { it.copy(expandedFilter = false)}
        }else{
            _uiState.update { it.copy(expandedFilter = true)}
        }
    }

    fun updateExpandedLongTap(){
        if(uiState.value.expandedLongTap){
            _uiState.update { it.copy(expandedLongTap = false)}
        }else{
            _uiState.update { it.copy(expandedLongTap = true)}
        }
    }

    fun loadData() {
        loadAllTask("")
        loadStatuses()
    }

    private fun loadAllTask(search: String) {
        viewModelScope.launch {
            majkoRepository.getAllUserTask(SearchTask(search)).collect() { response ->
                when (response) {
                    is ApiSuccess -> {
                        val fav: MutableList<TaskDataResponseUi> = mutableListOf()
                        val notFavorite: MutableList<TaskDataResponseUi> = mutableListOf()
                        response.data?.forEach { item ->
                            if (!item.isFavorite) {
                                notFavorite.add(item)
                            }else if(item.isFavorite){
                                fav.add(item)
                            }
                        }
                        _uiState.update { it.copy(
                            allTaskList = notFavorite.sortedBy { it.status },
                            searchAllTaskList = notFavorite.sortedBy { it.status },
                            favoritesTaskList = fav.sortedBy { it.status },
                            searchFavoritesTaskList = fav.sortedBy { it.status }) }
                    }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

    private fun loadFavTask(search: String) {
        viewModelScope.launch {
            majkoRepository.getAllUserTask(SearchTask(search)).collect() { response ->
                when (response) {
                    is ApiSuccess -> {
                        val fav: MutableList<TaskDataResponseUi> = mutableListOf()
                        response.data?.forEach { item ->
                            if(item.isFavorite){
                                fav.add(item)
                            }
                        }
                        _uiState.update { it.copy(
                            allTaskList = listOf(),
                            searchAllTaskList = listOf(),
                            favoritesTaskList = fav.sortedBy { it.status },
                            searchFavoritesTaskList = fav.sortedBy { it.status }) }
                    }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

    private fun loadEachTask(search: String) {
        viewModelScope.launch {
            majkoRepository.getAllUserTask(SearchTask(search)).collect() { response ->
                when (response) {
                    is ApiSuccess -> {
                        val notFavorite: MutableList<TaskDataResponseUi> = mutableListOf()
                        response.data?.forEach { item ->
                            if (!item.isFavorite) {
                                notFavorite.add(item)
                            }
                        }
                        _uiState.update { it.copy(
                            allTaskList = notFavorite.sortedBy { it.status },
                            searchAllTaskList = notFavorite.sortedBy { it.status },
                            favoritesTaskList = listOf(),
                            searchFavoritesTaskList = listOf()
                        ) }
                    }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

    private fun loadStatuses() {
        viewModelScope.launch {
            majkoInfoRepository.getStatuses().collect() { response ->
                when (response) {
                    is ApiSuccess -> {
                        _uiState.update { it.copy(statuses = response.data) }
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
                ?: uiState.value.favoritesTaskList?.find { it.id == id }

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
                ?: uiState.value.favoritesTaskList?.find { it.id == id }

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
