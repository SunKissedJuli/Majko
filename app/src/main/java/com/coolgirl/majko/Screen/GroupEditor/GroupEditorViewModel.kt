package com.coolgirl.majko.Screen.GroupEditor

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
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

class GroupEditorViewModel(private val dataStore: UserDataStore, private val group_id: String) : ViewModel() {
    private val _uiState = MutableStateFlow(GroupEditorUiState())
    val uiState: StateFlow<GroupEditorUiState> = _uiState.asStateFlow()

    init { loadData() }

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
        if(uiState.value.is_adding){
            _uiState.update { it.copy(is_adding = false) }
        }else{
            getProjectData()
            _uiState.update { it.copy(is_adding = true) }
        }
    }

    fun newInvite(){
        if(uiState.value.is_invite){
            _uiState.update { it.copy(is_invite = false) }
            _uiState.update { it.copy(is_invite_backgroun = 1f) }
        }else{
            _uiState.update { it.copy(is_invite = true) }
            _uiState.update { it.copy(is_invite_backgroun = 0.5f) }
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
        _uiState.update { it.copy(group_id = group_id) }
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            val call: Call<GroupResponse> = ApiClient().getGroupById("Bearer " + accessToken, GroupById(uiState.value.group_id))
            call.enqueue(object : Callback<GroupResponse> {
                override fun onResponse(call: Call<GroupResponse>, response: Response<GroupResponse>) {
                    _uiState.update { it.copy(groupData = response.body()!!) }

                }

                override fun onFailure(call: Call<GroupResponse>, t: Throwable) {
                    Log.d("tag", "Projectedit t = " + t.message)
                }
            })
        }
    }

    fun saveGroup(navHostController: NavHostController){
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            val call: Call<GroupResponse> = ApiClient().updateGroup("Bearer " + accessToken, GroupUpdate(uiState.value.group_id,
                uiState.value.groupData!!.title, uiState.value.groupData!!.description))
            call.enqueue(object : Callback<GroupResponse> {
                override fun onResponse(call: Call<GroupResponse>, response: Response<GroupResponse>) {
                    navHostController.navigate(Screen.Group.route)
                }

                override fun onFailure(call: Call<GroupResponse>, t: Throwable) {
                    Log.d("tag", "Projectedit response t" + t.message)
                }
            })
        }
    }

    fun removeGroup(navHostController: NavHostController){
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            val call: Call<Unit> = ApiClient().removeGroup("Bearer " + accessToken, GroupById(uiState.value.group_id))
            call.enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    navHostController.navigate(Screen.Group.route)
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Log.d("tag", "Projectedit response t" + t.message)
                }
            })
        }
    }

    fun saveProject(project_id: String){
        _uiState.update { it.copy(group_id = group_id) }
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            val call: Call<ProjectDataResponse> = ApiClient().addProjectInGroup("Bearer " + accessToken, ProjectInGroup(project_id, uiState.value.group_id))
            call.enqueue(object : Callback<ProjectDataResponse> {
                override fun onResponse(call: Call<ProjectDataResponse>, response: Response<ProjectDataResponse>) {
                    addingProject()
                    loadData()
                }

                override fun onFailure(call: Call<ProjectDataResponse>, t: Throwable) {
                    Log.d("tag", "Projectedit response t" + t.message)
                }
            })
        }
    }

    fun getProjectData(){
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            val call: Call<List<ProjectDataResponse>> =
                ApiClient().getPersonalProject("Bearer " + accessToken)
            call.enqueue(object : Callback<List<ProjectDataResponse>> {
                override fun onResponse(call: Call<List<ProjectDataResponse>>, response: Response<List<ProjectDataResponse>>) {
                    val validData: MutableList<ProjectDataResponse> = mutableListOf()
                    response.body()?.forEach { item ->
                        if (item.is_personal && item.is_archive == 0) {
                            validData.add(item)
                        }
                    }
                    _uiState.update { it.copy(projectData = validData) }
                }

                override fun onFailure(call: Call<List<ProjectDataResponse>>, t: Throwable) {
                    //дописать
                }
            })
        }
    }

    fun createInvite(){
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            val call: Call<GroupInviteResponse> = ApiClient().createInvitetoGroup("Bearer " + accessToken, GroupBy_Id(uiState.value.group_id))
            call.enqueue(object : Callback<GroupInviteResponse> {
                override fun onResponse(call: Call<GroupInviteResponse>, response: Response<GroupInviteResponse>) {
                    _uiState.update { it.copy(invite = response.body()!!.invite) }
                    newInvite()
                }

                override fun onFailure(call: Call<GroupInviteResponse>, t: Throwable) {
                    Log.d("tag", "Projectedit response t" + t.message)
                }
            })
        }
    }

}