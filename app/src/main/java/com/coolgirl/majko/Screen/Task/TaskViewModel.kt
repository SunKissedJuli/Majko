package com.coolgirl.majko.Screen.Task

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coolgirl.majko.R
import com.coolgirl.majko.commons.ApiError
import com.coolgirl.majko.commons.ApiExeption
import com.coolgirl.majko.commons.ApiSuccess
import com.coolgirl.majko.data.dataStore.UserDataStore
import com.coolgirl.majko.data.remote.dto.TaskData.TaskById
import com.coolgirl.majko.data.remote.dto.TaskData.TaskDataResponse
import com.coolgirl.majko.data.repository.MajkoInfoRepository
import com.coolgirl.majko.data.repository.MajkoTaskRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class TaskViewModel(private val majkoRepository: MajkoTaskRepository,
                    private val majkoInfoRepository: MajkoInfoRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState:StateFlow<TaskUiState> = _uiState.asStateFlow()

    fun updateSearchString(newSearchString:String){
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

    fun updateSearchString(newSearchString:String, whatFilter: Int){
        when (whatFilter) {
            0 -> { updateEachTask(newSearchString) }
            1 -> { updateFavTask(newSearchString) }
            else -> { updateAllTask(newSearchString) }
        }
    }

    fun updateEachTask(newSearchString:String){
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

    fun updateFavTask(newSearchString:String){
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

    fun updateAllTask(newSearchString:String){
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

    fun getPriority(priorityId: Int): Int{
        return when (priorityId) {
            1 -> R.color.green
            2 -> R.color.orange
            3 -> R.color.red
            else -> R.color.white
        }
    }

    fun getStatus(statusId: Int): String{
        if(!uiState.value.statuses.isNullOrEmpty()){
            for(item in uiState.value.statuses!!){
                if(item.id==statusId){
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

    fun loadFavTask(){
        viewModelScope.launch {
            majkoRepository.getAllFavorites().collect() { response ->
                when(response){
                    is ApiSuccess ->{
                        _uiState.update { it.copy(favoritesTaskList = response.data)}
                        _uiState.update { it.copy(searchFavoritesTaskList =  response.data)}
                    }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }

    }

    fun loadEachTask(){
        viewModelScope.launch {
            majkoRepository.getAllUserTask().collect() { response ->
                when(response){
                    is ApiSuccess ->{
                        val notFavorite: MutableList<TaskDataResponse> = mutableListOf()
                        response.data?.forEach { item ->
                            if (!item.is_favorite && item.mainTaskId==null) {
                                notFavorite.add(item)
                            }
                        }
                        _uiState.update { it.copy(allTaskList = notFavorite)}
                        _uiState.update { it.copy(searchAllTaskList = notFavorite)}
                    }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

    fun loadStatuses(){
        viewModelScope.launch {
            majkoInfoRepository.getStatuses().collect() { response ->
                when(response){
                    is ApiSuccess ->{
                        _uiState.update { it.copy(statuses = response.data!!) }
                    }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

    fun addFavotite(task_id: String){
        viewModelScope.launch {
            majkoRepository.addToFavorite(TaskById(task_id)).collect() { response ->
                when(response){
                    is ApiSuccess ->{ loadData() }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

    fun removeFavotite(task_id: String){
        viewModelScope.launch {
            majkoRepository.removeFavotire(TaskById(task_id)).collect() { response ->
                when(response){
                    is ApiSuccess ->{ loadData() }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }
}
