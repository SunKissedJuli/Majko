package com.coolgirl.majko.Screen.Task

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coolgirl.majko.R
import com.coolgirl.majko.data.dataStore.UserDataStore
import com.coolgirl.majko.data.remote.dto.MessageData
import com.coolgirl.majko.data.remote.dto.TaskData.TaskById
import com.coolgirl.majko.data.remote.dto.TaskData.TaskDataResponse
import com.coolgirl.majko.di.ApiClient
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TaskViewModel(private val dataStore : UserDataStore) : ViewModel() {
    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState:StateFlow<TaskUiState> = _uiState.asStateFlow()

    init{ loadData() }

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

    fun getPriority(priorityId: Int): Int{
        return when (priorityId) {
            1 -> R.color.green
            2 -> R.color.orange
            3 -> R.color.red
            else -> R.color.white
        }
    }

    fun getStatus(priorityId: Int): String{
        return when (priorityId) {
            1 -> "Не выбрано"
            2 -> "Обсуждается"
            3 -> "Ожидает"
            4 -> "В процессе"
            5 -> "Завершена"
            else -> "Нет статуса"
        }
    }

    fun loadData() {
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            val call: Call<List<TaskDataResponse>> = ApiClient().getAllUserTask("Bearer " + accessToken)
            call.enqueue(object : Callback<List<TaskDataResponse>> {
                override fun onResponse(call: Call<List<TaskDataResponse>>, response: Response<List<TaskDataResponse>>) {
                    if (response.code() == 200 && response.body()!=null) {
                        val notFavorite: MutableList<TaskDataResponse> = mutableListOf()
                        response.body()?.forEach { item ->
                            if (!item.is_favorite) {
                                notFavorite.add(item)
                            }
                        }
                        _uiState.update { it.copy(allTaskList = notFavorite)}
                        _uiState.update { it.copy(searchAllTaskList = notFavorite)}
                        Log.d("tag", "Taskeditor all = " + uiState.value.allTaskList)
                    }
                }

                override fun onFailure(call: Call<List<TaskDataResponse>>, t: Throwable) {
                    //дописать
                }
            })
            val call1: Call<List<TaskDataResponse>> = ApiClient().getAllFavorites("Bearer " + accessToken)
            call1.enqueue(object : Callback<List<TaskDataResponse>> {
                override fun onResponse(call1: Call<List<TaskDataResponse>>, response: Response<List<TaskDataResponse>>) {
                    if (response.code() == 200 && response.body()!=null) {
                        _uiState.update { it.copy(favoritesTaskList = response.body())}
                        _uiState.update { it.copy(searchFavoritesTaskList =  response.body())}
                        Log.d("tag", "Taskeditor fav = " + uiState.value.favoritesTaskList)
                    }
                }

                override fun onFailure(call1: Call<List<TaskDataResponse>>, t: Throwable) {
                    //дописать
                }
            })
        }
    }

    fun addFavotite(task_id: String){
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            val call: Call<MessageData> = ApiClient().addToFavorite("Bearer " + accessToken, TaskById(task_id))
            call.enqueue(object : Callback<MessageData> {
                override fun onResponse(call: Call<MessageData>, response: Response<MessageData>) {
                    if (response.code() == 200 && response.body() != null) {
                        loadData()
                    }
                }

                override fun onFailure(call: Call<MessageData>, t: Throwable) {
                    //дописать
                }
            })
        }
    }

    fun removeFavotite(task_id: String){
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            val call: Call<MessageData> = ApiClient().removeFavotire("Bearer " + accessToken, TaskById(task_id))
            call.enqueue(object : Callback<MessageData> {
                override fun onResponse(call: Call<MessageData>, response: Response<MessageData>) {
                    if (response.code() == 200 && response.body() != null) {
                        loadData()
                    }
                }

                override fun onFailure(call: Call<MessageData>, t: Throwable) {
                    //дописать
                }
            })
        }
    }
}
