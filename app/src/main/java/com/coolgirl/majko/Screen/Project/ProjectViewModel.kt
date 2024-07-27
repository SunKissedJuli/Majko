package com.coolgirl.majko.Screen.Project

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coolgirl.majko.commons.ApiError
import com.coolgirl.majko.commons.ApiExeption
import com.coolgirl.majko.commons.ApiSuccess
import com.coolgirl.majko.components.ProjectCardUiState
import com.coolgirl.majko.data.dataStore.UserDataStore
import com.coolgirl.majko.data.remote.dto.ProjectData.*
import com.coolgirl.majko.data.repository.MajkoProjectRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProjectViewModel(private val majkoRepository: MajkoProjectRepository) : ViewModel(){
    private val _uiState = MutableStateFlow(ProjectUiState())
    val uiState: StateFlow<ProjectUiState> = _uiState.asStateFlow()

    val _uiStateCard = MutableStateFlow(ProjectCardUiState())
    val uiStateCard: StateFlow<ProjectCardUiState> = _uiStateCard.asStateFlow()

    fun updateProjectName(name: String){
        _uiState.update { it.copy(newProjectName = name) }
    }

    fun updateProjectDescription(description: String){
        _uiState.update { it.copy(newProjectDescription = description) }
    }

    fun updateInvite(invite: String){
        _uiState.update { it.copy(invite = invite) }
    }

    fun openInviteWindow(){
        if(!uiState.value.isInvite){
            _uiState.update { it.copy(isInvite = true)}
        }else{
            _uiState.update { it.copy(isInvite = false)}
        }
    }

    fun addingProject(){
        _uiState.update { it.copy(isAdding = true)}
    }

    fun notAddingProjectYet(){
        _uiState.update { it.copy(isAdding = false)}
    }

   /* fun openPanel(id: String){
        if(id.isNotEmpty()){
            _uiState.update { it.copy(isLongtap = true) }
            _uiState.update { it.copy(longtapProjectId = uiState.value.longtapProjectId + id) }
        }else{
            _uiState.update { it.copy(isLongtap = false) }
            _uiState.update { it.copy(longtapProjectId = "") }
        }

    }*/

    fun openPanel(id: String) {
        if (uiState.value.longtapProjectId.contains(id)) {
            val updatedIds = uiState.value.longtapProjectId.split(",").filter { it != id }.joinToString(",")
            _uiState.update { it.copy(isLongtap = updatedIds.isNotEmpty(), longtapProjectId = updatedIds) }
        } else {
            val updatedIds = if (uiState.value.longtapProjectId.isEmpty()) id else "${uiState.value.longtapProjectId},$id"
            _uiState.update { it.copy(isLongtap = true, longtapProjectId = updatedIds) }
        }
    }

    fun updateSearchString(newSearchString:String, whatFilter: Int){
        when (whatFilter) {
            0 -> { updatePersonalProject(newSearchString) }
            1 -> { updateGroupProject(newSearchString) }
            else -> { updateAllProject(newSearchString) }
        }
    }

    fun updatePersonalProject(newSearchString:String){
        _uiState.update { currentState ->
            val filteredPersonalProject = currentState.personalProject?.filter { task ->
                task.name?.contains(newSearchString, ignoreCase = true) == true ||
                        task.description?.contains(newSearchString, ignoreCase = true) == true
            }

            currentState.copy(
                searchString = newSearchString,
                searchPersonalProject = filteredPersonalProject,
                searchGroupProject = null
            )
        }
    }

    fun updateGroupProject(newSearchString:String){
        _uiState.update { currentState ->

            val filteredGroupProject = currentState.groupProject?.filter { task ->
                task.name?.contains(newSearchString, ignoreCase = true) == true ||
                        task.description?.contains(newSearchString, ignoreCase = true) == true
            }

            currentState.copy(
                searchString = newSearchString,
                searchPersonalProject = null,
                searchGroupProject = filteredGroupProject
            )
        }
    }

    fun updateAllProject(newSearchString:String){
        _uiState.update { currentState ->
            val filteredPersonalProject = currentState.personalProject?.filter { task ->
                task.name?.contains(newSearchString, ignoreCase = true) == true ||
                        task.description?.contains(newSearchString, ignoreCase = true) == true
            }

            val filteredGroupProject = currentState.groupProject?.filter { task ->
                task.name?.contains(newSearchString, ignoreCase = true) == true ||
                        task.description?.contains(newSearchString, ignoreCase = true) == true
            }

            currentState.copy(
                searchString = newSearchString,
                searchPersonalProject = filteredPersonalProject,
                searchGroupProject = filteredGroupProject
            )
        }
    }

    fun joinByInvite(){
        viewModelScope.launch {
            majkoRepository.joinByInvitation(JoinByInviteProjectData(uiState.value.invite)).collect() { response ->
                when(response){
                    is ApiSuccess ->{
                        _uiState.update { it.copy(invite_message = response.data!!.message!!) }
                        loadData()
                    }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

    fun toArchive(){
        val projectIds = uiState.value.longtapProjectId.chunked(36) // Разделяем строку на ID длиной 36 символов
        projectIds.mapNotNull { id ->
            val project = uiState.value.personalProject?.find { it.id == id }
                ?: uiState.value.groupProject?.find { it.id == id }

            project?.let {
                val updateProject = ProjectUpdate(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    isArchive = 1
                )

                viewModelScope.launch {
                    majkoRepository.updateProject(updateProject).collect() { response ->
                        when(response){
                            is ApiSuccess ->{
                                openPanel("")
                                loadData()
                            }
                            is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                            is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                        }
                    }
                }
            }
        }
    }

    fun addProject(){
        viewModelScope.launch {
            majkoRepository.postNewProject(ProjectData(uiState.value.newProjectName, uiState.value.newProjectDescription)).collect() { response ->
                when(response){
                    is ApiSuccess ->{
                        notAddingProjectYet()
                        loadData()
                    }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

    fun loadData(){
        loadPersonalProject()
        loadGroupProject()
    }

    fun loadPersonalProject(){
        viewModelScope.launch {
            majkoRepository.getPersonalProject().collect() { response ->
                when(response){
                    is ApiSuccess ->{
                        val validData: MutableList<ProjectDataResponse> = mutableListOf()
                        response.data?.forEach { item ->
                            if (item.isPersonal && item.isArchive==0) {
                                validData.add(item)
                            }
                        }
                        _uiState.update { it.copy(personalProject = validData)}
                        _uiState.update { it.copy(searchPersonalProject = validData)}
                    }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

    fun loadGroupProject() {
        viewModelScope.launch {
            majkoRepository.getGroupProject().collect() { response ->
                when (response) {
                    is ApiSuccess -> {
                        val validData: MutableList<ProjectDataResponse> = mutableListOf()
                        response.data?.forEach { item ->
                            if (!item.isPersonal && item.isArchive == 0) {
                                validData.add(item)
                            }
                        }
                        _uiState.update { it.copy(groupProject = validData) }
                        _uiState.update { it.copy(searchGroupProject = validData) }
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