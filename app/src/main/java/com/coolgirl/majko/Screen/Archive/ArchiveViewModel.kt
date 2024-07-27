package com.coolgirl.majko.Screen.Archive

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coolgirl.majko.commons.ApiError
import com.coolgirl.majko.commons.ApiExeption
import com.coolgirl.majko.commons.ApiSuccess
import com.coolgirl.majko.components.ProjectCardUiState
import com.coolgirl.majko.data.dataStore.UserDataStore
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectDataResponse
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectUpdate
import com.coolgirl.majko.data.repository.MajkoProjectRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ArchiveViewModel(private val majkoRepository: MajkoProjectRepository) : ViewModel(){
    private val _uiState = MutableStateFlow(ArchiveUiState())
    val uiState: StateFlow<ArchiveUiState> = _uiState.asStateFlow()

    val _uiStateCard = MutableStateFlow(ProjectCardUiState())
    val uiStateCard: StateFlow<ProjectCardUiState> = _uiStateCard.asStateFlow()

    fun openPanel(id: String) {
        if (uiState.value.longtapProjectId.contains(id)) {
            val updatedIds = uiState.value.longtapProjectId.split(",").filter { it != id }.joinToString(",")
            _uiState.update { it.copy(isLongtap = updatedIds.isNotEmpty(), longtapProjectId = updatedIds) }
            Log.d("tag","id = " + uiState.value.longtapProjectId)
        } else {
           // val updatedIds = if (uiState.value.longtapProjectId.isEmpty()) id else "${uiState.value.longtapProjectId},$id"
            _uiState.update { it.copy(isLongtap = true, longtapProjectId = uiState.value.longtapProjectId + id) }
            Log.d("tag","id = " + uiState.value.longtapProjectId)
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

    fun fromArchive(){
        val projectIds = uiState.value.longtapProjectId.chunked(36) // Разделяем строку на ID длиной 36 символов
        projectIds.mapNotNull { id ->
            val project = uiState.value.personalProject?.find { it.id == id }
                ?: uiState.value.groupProject?.find { it.id == id }

            project?.let {
                val updateProject = ProjectUpdate(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    isArchive = 0
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
        _uiState.update { it.copy(longtapProjectId = "") }
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
                            if (item.isPersonal && item.isArchive==1) {
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
                            if (!item.isPersonal && item.isArchive == 1) {
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