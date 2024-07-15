package com.coolgirl.majko.Screen.Task

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coolgirl.majko.R
import com.coolgirl.majko.commons.RandomString
import com.coolgirl.majko.data.dataStore.UserDataStore
import com.coolgirl.majko.data.remote.dto.CurrentUserDataResponse
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

   // var allTaskList = MutableStateFlow<List<TaskDataResponse>?>(null)

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
                override fun onResponse(
                    call: Call<List<TaskDataResponse>>,
                    response: Response<List<TaskDataResponse>>) {
                    if (response.code() == 200) {
                        _uiState.update { it.copy(allTaskList = response.body()) }

                        Log.d("tag", "Taskeditor " + response.body())
                    }
                }

                override fun onFailure(call: Call<List<TaskDataResponse>>, t: Throwable) {
                    //дописать
                }
            })
        }
    }
}
