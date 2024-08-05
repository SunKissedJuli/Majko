package com.coolgirl.majko.Screen.Project

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coolgirl.majko.R
import com.coolgirl.majko.data.remote.ApiError
import com.coolgirl.majko.data.remote.ApiExeption
import com.coolgirl.majko.data.remote.ApiSuccess
import com.coolgirl.majko.components.ProjectCardUiState
import com.coolgirl.majko.data.remote.dto.ProjectData.*
import com.coolgirl.majko.data.repository.MajkoProjectRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProjectViewModel(private val majkoRepository: MajkoProjectRepository) : ViewModel(){
    private val _uiState = MutableStateFlow(ProjectUiState())
    val uiState: StateFlow<ProjectUiState> = _uiState.asStateFlow()

    private val _uiStateCard = MutableStateFlow(ProjectCardUiState())
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

    fun openPanel(id: String) {
        val idLength = 36
        val currentIds = uiState.value.longtapProjectId.chunked(idLength)
        val updatedIds = if (currentIds.contains(id)) {
            currentIds.filter { it != id }
        } else {
            currentIds + id
        }.joinToString("")

        _uiState.update { it.copy(isLongtap = updatedIds.isNotEmpty(), longtapProjectId = updatedIds) }
    }


    fun updateSearchString(newSearchString:String, whatFilter: Int){
        when (whatFilter) {
            0 -> { updatePersonalProject(newSearchString) }
            1 -> { updateGroupProject(newSearchString) }
            else -> { updateAllProject(newSearchString) }
        }
    }

    private fun updatePersonalProject(newSearchString:String){
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

    private fun updateGroupProject(newSearchString:String){
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

    private fun updateAllProject(newSearchString:String){
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

    fun isError(message: Int?){
        if(uiState.value.isError){
            _uiState.update { it.copy(isError = false)}
        }else{
            _uiState.update { it.copy(errorMessage = message, isError = true)}
        }
    }

    fun isMessage(message: Int?){
        if(uiState.value.isMessage){
            _uiState.update { it.copy(isMessage = false)}
        }else{
            _uiState.update { it.copy(message = message, isMessage = true)}
        }
    }

    fun updateExpandedFilter(){
        if(uiState.value.expandedFilter){
            _uiState.update { it.copy(expandedFilter = false)}
        }else{
            _uiState.update { it.copy(expandedFilter = true)}
        }
    }

    fun updateExpanded(){
        if(uiState.value.expanded){
            _uiState.update { it.copy(expanded = false)}
        }else{
            _uiState.update { it.copy(expanded = true)}
        }
    }

    fun updateExpandedLongTap(){
        if(uiState.value.expandedLongTap){
            _uiState.update { it.copy(expandedLongTap = false)}
        }else{
            _uiState.update { it.copy(expandedLongTap = true)}
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
                    else -> {}
                }
            }
        }
    }

    fun toArchive() {
        val projectIds = uiState.value.longtapProjectId.chunked(36)
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
                    majkoRepository.updateProject(updateProject).collect { response ->
                        when(response){
                            is ApiSuccess -> {
                                isMessage(R.string.message_toarchive)
                                loadData()
                            }
                            is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                            is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                            else -> {}
                        }
                    }
                }
            }
        }

        _uiState.update { it.copy(longtapProjectId = "", isLongtap = false) }
        loadData()
    }

    fun removeProjects() {
        val projectIds = uiState.value.longtapProjectId.chunked(36)
        projectIds.mapNotNull { id ->
            val project = uiState.value.personalProject?.find { it.id == id }
                ?: uiState.value.groupProject?.find { it.id == id }

            project?.let {
                val removeProject = ProjectById(it.id)

                viewModelScope.launch {
                    majkoRepository.removeProject(removeProject).collect { response ->
                        when(response){
                            is ApiSuccess -> {
                                isMessage(R.string.message_remove_project)
                                loadData()}
                            is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                            is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                            else -> {}
                        }
                    }
                }
            }
        }

        _uiState.update { it.copy(longtapProjectId = "", isLongtap = false) }
        loadData()
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
                    else -> {}
                }
            }
        }
    }

    fun loadData(){
        loadPersonalProject()
        loadGroupProject()
    }

    private fun loadPersonalProject(){
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
                        _uiState.update { it.copy(personalProject = validData, searchPersonalProject = validData)}
                    }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                    else -> {}
                }
            }
        }
    }

    private fun loadGroupProject() {
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
                        _uiState.update { it.copy(groupProject = validData, searchGroupProject = validData) }
                    }
                    is ApiError -> {
                        Log.d("TAG", "error message = " + response.message)
                    }
                    is ApiExeption -> {
                        Log.d("TAG", "exeption e = " + response.e)
                    }
                    else -> {}
                }
            }
        }
    }
}