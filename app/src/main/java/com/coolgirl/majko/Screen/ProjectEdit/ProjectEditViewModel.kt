package com.coolgirl.majko.Screen.ProjectEdit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.coolgirl.majko.R
import com.coolgirl.majko.commons.ApiError
import com.coolgirl.majko.commons.ApiExeption
import com.coolgirl.majko.commons.ApiSuccess
import com.coolgirl.majko.components.SpinnerItems
import com.coolgirl.majko.data.MajkoRepository
import com.coolgirl.majko.data.dataStore.UserDataStore
import com.coolgirl.majko.data.remote.dto.ProjectData.*
import com.coolgirl.majko.data.remote.dto.TaskData.TaskById
import com.coolgirl.majko.data.remote.dto.TaskData.TaskData
import com.coolgirl.majko.data.remote.dto.TaskData.TaskDataResponse
import com.coolgirl.majko.di.ApiClient
import com.coolgirl.majko.navigation.Screen
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProjectEditViewModel(private val dataStore: UserDataStore, private val majkoRepository: MajkoRepository, private val projectId : String) : ViewModel() {
    private val _uiState = MutableStateFlow(ProjectEditUiState())
    val uiState: StateFlow<ProjectEditUiState> = _uiState.asStateFlow()

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
        if(uiState.value.isAdding){
            _uiState.update { it.copy(isAdding = false) }
        }else{
            _uiState.update { it.copy(isAdding = true) }
        }
    }

    fun newInvite(){
        if(uiState.value.isInvite){
            _uiState.update { it.copy(isInvite = false) }
        }else{
            _uiState.update { it.copy(isInvite = true) }
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


    fun getStatus() : List<SpinnerItems>{
        val list = mutableListOf<SpinnerItems>()
        if (!uiState.value.statuses.isNullOrEmpty()) {
            for (status in uiState.value.statuses!!) {
                list.add(SpinnerItems(status.id.toString(), status.name))
            }
        }
        return list
    }

    fun getPriority() : List<SpinnerItems>{
        val list = mutableListOf<SpinnerItems>()
        if (!uiState.value.proprieties.isNullOrEmpty()) {
            for (propriety in uiState.value.proprieties!!) {
                list.add(SpinnerItems(propriety.id.toString(), propriety.name))
            }
        }
        return list
    }

    fun getStatusName(statusId: Int) : String{
        if(!uiState.value.statuses.isNullOrEmpty()){
            for(item in uiState.value.statuses!!){
                if(item.id==statusId){
                    return item.name
                }
            }
        }
        return "Нет"
    }

    fun getPriorityName(priorityId: Int) : String{
        if(!uiState.value.proprieties.isNullOrEmpty()){
            for(item in uiState.value.proprieties!!){
                if(item.id==priorityId){
                    return item.name
                }
            }
        }
        return "Нет"
    }

    fun loadData(){
        _uiState.update { it.copy(projectId = projectId) }
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            majkoRepository.getProjectById("Bearer " + accessToken, ProjectById(uiState.value.projectId)).collect() { response ->
                when(response){
                    is ApiSuccess ->{
                        _uiState.update { it.copy(projectData = response.data!!) }
                        if(response.data!!.members.isNotEmpty()){
                            _uiState.update { it.copy(members = response.data!!.members) }
                        }
                    }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
            majkoRepository.getStatuses().collect() { response ->
                when(response){
                    is ApiSuccess ->{
                        _uiState.update { it.copy(statuses = response.data!!) }
                    }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
            majkoRepository.getPriorities().collect() { response ->
                when(response){
                    is ApiSuccess ->{
                        _uiState.update { it.copy(proprieties = response.data!!) }
                    }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

    fun saveProject(navHostController: NavHostController){
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            val updateProject = ProjectUpdate(uiState.value.projectId, uiState.value.projectData!!.name,uiState.value.projectData!!.description,0)
            majkoRepository.updateProject("Bearer " + accessToken, updateProject).collect() { response ->
                when(response){
                    is ApiSuccess ->{ navHostController.navigate(Screen.Project.route) }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

    fun removeProject(navHostController: NavHostController){
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            majkoRepository.removeProject("Bearer " + accessToken,  ProjectById(uiState.value.projectId)).collect() { response ->
                when(response){
                    is ApiSuccess ->{ navHostController.navigate(Screen.Project.route) }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

    fun saveTask(){
        _uiState.update { it.copy(projectId = projectId) }
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            val newTask = TaskData(uiState.value.taskName, uiState.value.taskText,uiState.value.taskDeadline,
                uiState.value.taskPriority,uiState.value.taskStatus,uiState.value.projectId,"")
            majkoRepository.postNewTask("Bearer " + accessToken, newTask).collect() { response ->
                when(response){
                    is ApiSuccess ->{
                        addingTask()
                        loadData()
                    }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

    fun createInvite(){
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            majkoRepository.createInvitetoProject("Bearer " + accessToken,  ProjectBy_Id(uiState.value.projectId)).collect() { response ->
                when(response){
                    is ApiSuccess ->{
                        _uiState.update { it.copy(invite = response.data!!.invite) }
                        newInvite()
                    }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }
}