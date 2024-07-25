package com.coolgirl.majko.Screen.GroupEditor

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.coolgirl.majko.commons.ApiError
import com.coolgirl.majko.commons.ApiExeption
import com.coolgirl.majko.commons.ApiSuccess
import com.coolgirl.majko.data.MajkoRepository
import com.coolgirl.majko.data.dataStore.UserDataStore
import com.coolgirl.majko.data.remote.dto.GroupData.*
import com.coolgirl.majko.data.remote.dto.ProjectData.*
import com.coolgirl.majko.di.ApiClient
import com.coolgirl.majko.navigation.Screen
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupEditorViewModel(private val dataStore: UserDataStore, private val majkoRepository: MajkoRepository, private val groupId: String) : ViewModel() {
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

    fun updateGroupDescription(description: String){
        _uiState.update { currentState ->
            currentState.groupData?.let { currentProjectData ->
                currentState.copy(
                    groupData = currentProjectData.copy(description = description)
                )
            } ?: currentState
        }
    }

    fun loadData(){
        _uiState.update { it.copy(groupId = groupId) }
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            majkoRepository.getGroupById("Bearer " + accessToken,  GroupById(uiState.value.groupId)).collect() { response ->
                when(response){
                    is ApiSuccess ->{
                        _uiState.update { it.copy(groupData = response.data!!) }
                    }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

    fun saveGroup(navHostController: NavHostController){
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            majkoRepository.updateGroup("Bearer " + accessToken,  GroupUpdate(uiState.value.groupId,
                uiState.value.groupData!!.title, uiState.value.groupData!!.description)).collect() { response ->
                when(response){
                    is ApiSuccess ->{ navHostController.navigate(Screen.Group.route) }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

    fun removeGroup(navHostController: NavHostController){
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            majkoRepository.removeGroup("Bearer " + accessToken,  GroupById(uiState.value.groupId)).collect() { response ->
                when(response){
                    is ApiSuccess ->{ navHostController.navigate(Screen.Group.route) }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

    fun saveProject(project_id: String){
        _uiState.update { it.copy(groupId = groupId) }
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            majkoRepository.addProjectInGroup("Bearer " + accessToken, ProjectInGroup(project_id, uiState.value.groupId)).collect() { response ->
                when(response){
                    is ApiSuccess ->{
                        addingProject()
                        loadData() }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

    fun getProjectData(){
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            majkoRepository.getPersonalProject("Bearer " + accessToken).collect() { response ->
                when(response){
                    is ApiSuccess ->{
                        val validData: MutableList<ProjectDataResponse> = mutableListOf()
                        response.data?.forEach { item ->
                            if (item.is_personal && item.is_archive == 0) {
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
            val accessToken = dataStore.getAccessToken().first() ?: ""
            majkoRepository.createInvitetoGroup("Bearer " + accessToken, GroupBy_Id(uiState.value.groupId)).collect() { response ->
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