package com.coolgirl.majko.Screen.GroupEditor

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.coolgirl.majko.commons.ApiError
import com.coolgirl.majko.commons.ApiExeption
import com.coolgirl.majko.commons.ApiSuccess
import com.coolgirl.majko.data.dataStore.UserDataStore
import com.coolgirl.majko.data.remote.dto.GroupData.*
import com.coolgirl.majko.data.remote.dto.ProjectData.*
import com.coolgirl.majko.data.repository.MajkoGroupRepository
import com.coolgirl.majko.data.repository.MajkoProjectRepository
import com.coolgirl.majko.navigation.Screen
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GroupEditorViewModel(private val majkoRepository: MajkoGroupRepository,
                           private val majkoProjectRepository: MajkoProjectRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(GroupEditorUiState())
    val uiState: StateFlow<GroupEditorUiState> = _uiState.asStateFlow()

    fun updateGroupName(name: String){
        _uiState.update { currentState ->
            currentState.groupData?.let { currentProjectData ->
                currentState.copy(
                    groupData = currentProjectData.copy(title = name)
                )
            } ?: currentState
        }
    }

    fun addingProject(){
        if(uiState.value.isAdding){
            _uiState.update { it.copy(isAdding = false) }
        }else{
            getProjectData()
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

    fun showMembers(){
        if(uiState.value.isMembers){
            _uiState.update { it.copy(isMembers = false) }
        }else{
            _uiState.update { it.copy(isMembers = true) }
        }
    }


    fun updateGroupDescription(description: String){
        _uiState.update { currentState ->
            currentState.groupData?.let { currentProjectData ->
                currentState.copy(
                    groupData = currentProjectData.copy(description = description)
                )
            } ?: currentState
        }
    }

    fun loadData(groupId: String){
        _uiState.update { it.copy(groupId = groupId) }
        viewModelScope.launch {
            majkoRepository.getGroupById(GroupById(uiState.value.groupId)).collect() { response ->
                when(response){
                    is ApiSuccess ->{
                        _uiState.update { it.copy(groupData = response.data!!) }
                        if(!response.data.members.isNullOrEmpty()){
                            _uiState.update { it.copy(members = response.data!!.members) }
                        }
                    }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

    fun saveGroup(navHostController: NavHostController){
        navHostController.navigate(Screen.Group.route)
        viewModelScope.launch {
            majkoRepository.updateGroup(GroupUpdate(uiState.value.groupId,
                uiState.value.groupData!!.title, uiState.value.groupData!!.description)).collect() { response ->
                when(response){
                    is ApiSuccess ->{  }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

    fun removeGroup(navHostController: NavHostController){
        navHostController.navigate(Screen.Group.route)
        viewModelScope.launch {
            majkoRepository.removeGroup(GroupById(uiState.value.groupId)).collect() { response ->
                when(response){
                    is ApiSuccess -> { }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

    fun saveProject(project_id: String){
        viewModelScope.launch {
            majkoRepository.addProjectInGroup(ProjectInGroup(project_id, uiState.value.groupId)).collect() { response ->
                when(response){
                    is ApiSuccess ->{
                        addingProject()
                        loadData(uiState.value.groupId) }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

    fun getProjectData(){
        viewModelScope.launch {
            majkoProjectRepository.getPersonalProject().collect() { response ->
                when(response){
                    is ApiSuccess ->{
                        val validData: MutableList<ProjectDataResponse> = mutableListOf()
                        response.data?.forEach { item ->
                            if (item.isPersonal && item.isArchive == 0) {
                                validData.add(item)
                            }
                        }
                        _uiState.update { it.copy(projectData = validData) } }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

    fun createInvite(){
        viewModelScope.launch {
            majkoRepository.createInvitetoGroup(GroupByIdUnderscore(uiState.value.groupId)).collect() { response ->
                when(response){
                    is ApiSuccess ->{
                        _uiState.update { it.copy(invite = response.data!!.invite) }
                        newInvite()}
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

}