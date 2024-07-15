package com.coolgirl.majko.Screen.TaskEditor

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.coolgirl.majko.commons.SpinnerItems
import com.coolgirl.majko.data.dataStore.UserDataStore
import com.coolgirl.majko.data.remote.dto.TaskData.TaskDataResponse
import com.coolgirl.majko.di.ApiClient
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import androidx.lifecycle.viewModelScope
import com.coolgirl.majko.data.remote.dto.TaskData.TaskData
import com.coolgirl.majko.navigation.Screen
import retrofit2.Response

class TaskEditorViewModel(private val dataStore : UserDataStore, private val task_id : Int) : ViewModel(){
    private val _uiState = MutableStateFlow(TaskEditorUiState())
    val uiState: StateFlow<TaskEditorUiState> = _uiState.asStateFlow()

    fun updateTaskText(text: String) {
        _uiState.update { it.copy(taskText = text) }
    }

    fun updateTaskName(name:String){
        _uiState.update { it.copy(taskName = name) }
    }

    fun updateTaskPriority(prioryti:String){
        _uiState.update { it.copy(taskPriority = prioryti.toInt()) }
    }

    fun updateTaskStatus(status:String){
        _uiState.update { it.copy(taskStatus = status.toInt()) }
    }

    fun updateTaskProject(project:String){
        _uiState.update { it.copy(taskProject = project) }
    }

    fun updateTaskDeadlie(deadline:String){
        _uiState.update { it.copy(taskDeadline = deadline) }
        Log.d("tag", "Taskeditor updateTaskDeadlie = " + _uiState.value.taskDeadline)
    }

    fun getStatus() : List<SpinnerItems>{
        return listOf(
            SpinnerItems("1", "Не выбрано"),
            SpinnerItems("2", "Обсуждается"),
            SpinnerItems("3", "Ожидает"),
            SpinnerItems("4", "В процессе"),
            SpinnerItems("5", "Завершена")
        )
    }

    fun getPriority() : List<SpinnerItems>{
        return listOf(
            SpinnerItems("1", "Низкий"),
            SpinnerItems("2", "Средний"),
            SpinnerItems("3", "Высокий")
        )
    }

    fun saveTask(navHostController: NavHostController){
        Log.d("tag", "Taskeditor это savetask")
        viewModelScope.launch {
            Log.d("tag", "Taskeditor это savetask launch")
            val accessToken = dataStore.getAccessToken().first() ?: ""
            val newTask = TaskData(uiState.value.taskName, uiState.value.taskText,uiState.value.taskDeadline,
                uiState.value.taskPriority,uiState.value.taskStatus,uiState.value.taskProject,"")
            val call: Call<TaskDataResponse> = ApiClient().postNewTask("Bearer " + accessToken, newTask)
            call.enqueue(object : Callback<TaskDataResponse> {
                override fun onResponse(call: Call<TaskDataResponse>, response: Response<TaskDataResponse>) {
                    if (response.code() == 200||response.code()==201) {
                        navHostController.navigate(Screen.Task.route)
                    }

                }

                override fun onFailure(call: Call<TaskDataResponse>, t: Throwable) {
                    Log.d("tag", "Taskeditor response t" + t.message)
                }
            })
        }
    }

    fun loadData() {

    }
}