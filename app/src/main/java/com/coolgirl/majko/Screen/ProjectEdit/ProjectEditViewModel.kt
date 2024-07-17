package com.coolgirl.majko.Screen.ProjectEdit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.coolgirl.majko.R
import com.coolgirl.majko.commons.SpinnerItems
import com.coolgirl.majko.data.dataStore.UserDataStore
import com.coolgirl.majko.data.remote.dto.MessageData
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectById
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectCurrentResponse
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectUpdate
import com.coolgirl.majko.data.remote.dto.TaskData.TaskData
import com.coolgirl.majko.data.remote.dto.TaskData.TaskDataResponse
import com.coolgirl.majko.di.ApiClient
import com.coolgirl.majko.navigation.Screen
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProjectEditViewModel(private val dataStore: UserDataStore, private val project_id : String) : ViewModel() {
    private val _uiState = MutableStateFlow(ProjectEditUiState())
    val uiState: StateFlow<ProjectEditUiState> = _uiState.asStateFlow()

    init { loadData() }

    fun updateProjectName(name: String){
        _uiState.update { currentState ->
            currentState.projectData?.let { currentProjectData ->
                currentState.copy(
                    projectData = currentProjectData.copy(name = name)
                )
            } ?: currentState
        }
    }

    fun addingTask(){
        if(uiState.value.is_adding){
            _uiState.update { it.copy(is_adding = false) }
        }else{
            _uiState.update { it.copy(is_adding = true) }
        }
    }

    fun updateProjectDescription(description: String){
        _uiState.update { currentState ->
            currentState.projectData?.let { currentProjectData ->
                currentState.copy(
                    projectData = currentProjectData.copy(description = description)
                )
            } ?: currentState
        }
    }

    fun updateTaskText(text: String) {
        _uiState.update { it.copy(taskText = text) }
    }

    fun updateTaskName(name:String){
        _uiState.update { it.copy(taskName = name) }
    }

    fun updateTaskStatus(status:String){
        _uiState.update { it.copy(taskStatus = status.toInt()) }
    }

    fun updateTaskDeadlie(deadline:String){
        _uiState.update { it.copy(taskDeadline = deadline) }
        Log.d("tag", "Taskeditor updateTaskDeadlie = " + _uiState.value.taskDeadline)
    }

    fun updateTaskPriority(prioryti:String){
        _uiState.update { it.copy(taskPriority = prioryti.toInt()) }
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

    fun loadData(){
        _uiState.update { it.copy(projectId = project_id) }
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            val call: Call<ProjectCurrentResponse> = ApiClient().getProjectById("Bearer " + accessToken, ProjectById(uiState.value.projectId))
            call.enqueue(object : Callback<ProjectCurrentResponse> {
                override fun onResponse(call: Call<ProjectCurrentResponse>, response: Response<ProjectCurrentResponse>) {
                    if (response.code() == 200||response.code()==201) {
                        _uiState.update { it.copy(projectData = response.body()!!) }
                    }
                }

                override fun onFailure(call: Call<ProjectCurrentResponse>, t: Throwable) {
                    Log.d("tag", "ProjectEditlog t = " + t.message)
                }
            })
        }
    }

    fun saveProject(navHostController: NavHostController){
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            val updateProject = ProjectUpdate(uiState.value.projectId, uiState.value.projectData!!.name,uiState.value.projectData!!.description,0)
            val call: Call<ProjectCurrentResponse> = ApiClient().updateProject("Bearer " + accessToken, updateProject)
            call.enqueue(object : Callback<ProjectCurrentResponse> {
                override fun onResponse(call: Call<ProjectCurrentResponse>, response: Response<ProjectCurrentResponse>) {
                    if (response.code() == 200||response.code()==201) {
                       navHostController.navigate(Screen.Project.route)
                    }
                }
                override fun onFailure(call: Call<ProjectCurrentResponse>, t: Throwable) {
                    Log.d("tag", "Taskeditor response t" + t.message)
                }
            })
        }
    }

    fun removeProject(navHostController: NavHostController){
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            val call: Call<Unit> = ApiClient().removeProject("Bearer " + accessToken, ProjectById(uiState.value.projectId))
            call.enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (response.code() == 200||response.code()==201) {
                        Log.d("tag", "Taskeditor response 200 " + response.body())
                        navHostController.navigate(Screen.Project.route)
                    }
                    Log.d("tag", "Taskeditor response не200 " + response.code())
                }
                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Log.d("tag", "Taskeditor response t" + t.message)
                }
            })
        }
    }

    fun saveTask(){
        _uiState.update { it.copy(projectId = project_id) }
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            val newTask = TaskData(uiState.value.taskName, uiState.value.taskText,uiState.value.taskDeadline,
                uiState.value.taskPriority,uiState.value.taskStatus,uiState.value.projectId,"")
            val call: Call<TaskDataResponse> = ApiClient().postNewTask("Bearer " + accessToken, newTask)
            call.enqueue(object : Callback<TaskDataResponse> {
                override fun onResponse(call: Call<TaskDataResponse>, response: Response<TaskDataResponse>) {
                    if (response.code() == 200||response.code()==201) {
                        addingTask()
                        loadData()
                    }
                }
                override fun onFailure(call: Call<TaskDataResponse>, t: Throwable) {
                    Log.d("tag", "Taskeditor response t" + t.message)
                }
            })
        }
    }
}