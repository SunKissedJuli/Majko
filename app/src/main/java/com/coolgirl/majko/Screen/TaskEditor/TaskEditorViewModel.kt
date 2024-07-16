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
import com.coolgirl.majko.R
import com.coolgirl.majko.data.remote.dto.TaskData.TaskById
import com.coolgirl.majko.data.remote.dto.TaskData.TaskBy_Id
import com.coolgirl.majko.data.remote.dto.TaskData.TaskData
import com.coolgirl.majko.navigation.Screen
import retrofit2.Response

class TaskEditorViewModel(private val dataStore : UserDataStore, private val task_id : String) : ViewModel(){
    private val _uiState = MutableStateFlow(TaskEditorUiState())
    val uiState: StateFlow<TaskEditorUiState> = _uiState.asStateFlow()

    init{loadData()}

    fun updateTaskText(text: String) {
        _uiState.update { it.copy(taskText = text) }
    }

    fun updateTaskName(name:String){
        _uiState.update { it.copy(taskName = name) }
    }

    fun updateTaskPriority(prioryti:String){
        _uiState.update { it.copy(taskPriority = prioryti.toInt()) }
        when (prioryti.toInt()) {
            1 -> _uiState.update { it.copy(backgroundColor = R.color.green) }
            2 -> _uiState.update { it.copy(backgroundColor = R.color.orange) }
            3 -> _uiState.update { it.copy(backgroundColor = R.color.red) }
            else -> _uiState.update { it.copy(backgroundColor = R.color.white) }
        }
    }

    fun updateTaskStatus(status:String){
        _uiState.update { it.copy(taskStatus = status.toInt()) }
    }

    fun updateTaskProject(project:String){
        _uiState.update { it.copy(taskProject = project) }
    }

    fun updateTaskDeadlie(deadline:String){
        _uiState.update { it.copy(taskDeadline = deadline) }
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

    fun getStatusName(status: Int) : String{
        return when (status) {
            1 -> "Не выбрано"
            2 -> "Обсуждается"
            3 -> "Ожидает"
            4 -> "В процессе"
            5 -> "Завершена"
            else -> "Нет"
        }
    }

    fun getPriorityName(priority: Int) : String{
        return when (priority) {
            1 -> "Низкий"
            2 -> "Средний"
            3 -> "Высокий"
            else -> "Нет"
        }
    }

    fun saveTask(navHostController: NavHostController){
        viewModelScope.launch {
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

    fun removeTask(navHostController: NavHostController){
       if(!task_id.equals("0")) {
           viewModelScope.launch {
               val accessToken = dataStore.getAccessToken().first() ?: ""
               val call: Call<Unit> = ApiClient().removeTask("Bearer " + accessToken, TaskBy_Id(task_id))
               call.enqueue(object : Callback<Unit> {
                   override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                       if (response.code() == 200 || response.code() == 201) {
                           navHostController.navigate(Screen.Task.route)

                       }
                   }

                   override fun onFailure(call: Call<Unit>, t: Throwable) {
                       Log.d("tag", "Taskeditor не 200 t" + t.message)
                   }
               })
           }
       }
    }

    fun loadData() {
        if(!task_id.equals("0")){
            _uiState.update { it.copy(taskId = task_id) }
            viewModelScope.launch {
                val accessToken = dataStore.getAccessToken().first() ?: ""
                val call: Call<TaskDataResponse> = ApiClient().getTaskById("Bearer " + accessToken, TaskById(uiState.value.taskId))
                call.enqueue(object : Callback<TaskDataResponse> {
                    override fun onResponse(call: Call<TaskDataResponse>, response: Response<TaskDataResponse>) {
                        if (response.code() == 200||response.code()==201) {
                            _uiState.update { it.copy(taskId = task_id) }
                            _uiState.update { it.copy(taskDeadline = response.body()!!.deadline) }
                            if(response.body()?.project !=null){
                                _uiState.update { it.copy(taskProject = response.body()!!.project!!.id) }
                            }
                            updateTaskPriority(response.body()!!.priority!!.toString())
                            _uiState.update { it.copy(taskName = response.body()!!.title!!) }
                            _uiState.update { it.copy(taskText = response.body()!!.text!!) }
                            _uiState.update { it.copy(taskStatus = response.body()!!.status!!) }
                        }
                    }

                    override fun onFailure(call: Call<TaskDataResponse>, t: Throwable) {
                        Log.d("tag", "Taskeditor response t" + t.message)
                    }
                })
            }
        }

    }
}