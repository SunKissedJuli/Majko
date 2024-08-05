package com.coolgirl.majko.Screen.ProjectEdit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.coolgirl.majko.R
import com.coolgirl.majko.data.remote.ApiError
import com.coolgirl.majko.data.remote.ApiExeption
import com.coolgirl.majko.data.remote.ApiSuccess
import com.coolgirl.majko.components.SpinnerItems
import com.coolgirl.majko.data.remote.dto.ProjectData.*
import com.coolgirl.majko.data.remote.dto.TaskData.TaskData
import com.coolgirl.majko.data.repository.MajkoInfoRepository
import com.coolgirl.majko.data.repository.MajkoProjectRepository
import com.coolgirl.majko.data.repository.MajkoTaskRepository
import com.coolgirl.majko.navigation.Screen
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProjectEditViewModel(private val majkoRepository: MajkoProjectRepository,
                           private val majkoInfoRepository: MajkoInfoRepository,
                           private val majkoTaskRepository: MajkoTaskRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(ProjectEditUiState())
    val uiState: StateFlow<ProjectEditUiState> = _uiState.asStateFlow()

    fun updateProjectName(name: String){
        _uiState.update { currentState ->
            currentState.projectData?.let { currentProjectData ->
                currentState.copy(projectData = currentProjectData.copy(name = name))
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

    fun updateExitDialog(){
        if(uiState.value.exitDialog){
            _uiState.update { it.copy(exitDialog = false) }
        }
        else{
            _uiState.update { it.copy(exitDialog = true) }
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

    fun getPriority(priorityId: Int): Int {
        return when (priorityId) {
            1 -> R.color.green
            2 -> R.color.yellow
            3 -> R.color.orange
            4 -> R.color.red
            else -> R.color.white
        }
    }

    fun showMembers(){
        if(uiState.value.isMembers){
            _uiState.update { it.copy(isMembers = false) }
        }else{
            _uiState.update { it.copy(isMembers = true) }
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
        return R.string.common_no.toString()
    }

    fun getPriorityName(priorityId: Int) : String{
        if(!uiState.value.proprieties.isNullOrEmpty()){
            for(item in uiState.value.proprieties!!){
                if(item.id==priorityId){
                    return item.name
                }
            }
        }
        return R.string.common_no.toString()
    }

    fun updateExpanded(){
        if(uiState.value.expanded){
            _uiState.update { it.copy(expanded = false)}
        }else{
            _uiState.update { it.copy(expanded = true)}
        }
    }

    fun loadData(projectId: String){
        _uiState.update { it.copy(projectId = projectId) }
        loadProject()
        loadPriorities()
        loadStatuses()
    }

    private fun loadProject(){
        viewModelScope.launch {
            majkoRepository.getProjectById(ProjectById(uiState.value.projectId)
            ).collect() { response ->
                when (response) {
                    is ApiSuccess -> {
                        _uiState.update { it.copy(projectData = response.data!!) }
                        if (response.data!!.members.isNotEmpty()) {
                            _uiState.update { it.copy(members = response.data!!.members) }
                        }
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

    private fun loadStatuses(){
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

    private fun loadPriorities(){
        viewModelScope.launch {
            majkoInfoRepository.getPriorities().collect() { response ->
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
        navHostController.popBackStack()
        viewModelScope.launch {
            val updateProject = ProjectUpdate(uiState.value.projectId, uiState.value.projectData!!.name,
                uiState.value.projectData!!.description, uiState.value.projectData!!.isArchive)
            majkoRepository.updateProject(updateProject).collect() { response ->
                when(response){
                    is ApiSuccess ->{  }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message)
                        navHostController.navigate(Screen.Project.route)}
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e)
                        navHostController.navigate(Screen.Project.route) }
                }
            }
        }
    }

    fun removeProject(navHostController: NavHostController){
        navHostController.popBackStack()
        viewModelScope.launch {
            majkoRepository.removeProject(ProjectById(uiState.value.projectId)).collect() { response ->
                when(response){
                    is ApiSuccess ->{ }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

    fun saveTask(){
        viewModelScope.launch {
            val newTask = TaskData(uiState.value.taskName, uiState.value.taskText,uiState.value.taskDeadline,
                uiState.value.taskPriority,uiState.value.taskStatus,uiState.value.projectId,"")
            majkoTaskRepository.postNewTask(newTask).collect() { response ->
                when(response){
                    is ApiSuccess ->{
                        addingTask()
                        loadData(uiState.value.projectId)
                        _uiState.update { it.copy(
                            taskDeadline = "",
                            taskName = "",
                            taskText = "",
                            taskPriority = 1,
                            taskStatus = 1) }
                    }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

    fun createInvite(){
        viewModelScope.launch {
            majkoRepository.createInvitetoProject(ProjectByIdUnderscore(uiState.value.projectId)).collect() { response ->
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