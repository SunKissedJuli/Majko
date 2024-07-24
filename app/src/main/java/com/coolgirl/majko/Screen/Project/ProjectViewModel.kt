package com.coolgirl.majko.Screen.Project

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coolgirl.majko.components.ProjectCardUiState
import com.coolgirl.majko.data.dataStore.UserDataStore
import com.coolgirl.majko.data.remote.dto.MessageData
import com.coolgirl.majko.data.remote.dto.ProjectData.*
import com.coolgirl.majko.di.ApiClient
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProjectViewModel(private val dataStore : UserDataStore) : ViewModel(){
    private val _uiState = MutableStateFlow(ProjectUiState())
    val uiState: StateFlow<ProjectUiState> = _uiState.asStateFlow()

    val _uiStateCard = MutableStateFlow(ProjectCardUiState())
    val uiStateCard: StateFlow<ProjectCardUiState> = _uiStateCard.asStateFlow()

    init{loadData()}

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
        if(uiState.value.isInvite==false){
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

    fun openPanel(id: String){
        if(id!=""){
            _uiState.update { it.copy(isLongtap = true) }
            _uiState.update { it.copy(longtapProjectId = uiState.value.longtapProjectId + id) }
        }else{
            _uiState.update { it.copy(isLongtap = false) }
            _uiState.update { it.copy(longtapProjectId = "") }
        }

    }

    fun updateSearchString(newSearchString:String){
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
            val accessToken = dataStore.getAccessToken().first() ?: ""
            val call: Call<MessageData> = ApiClient().joinByInvitation("Bearer " + accessToken, JoinByInviteProjectData(uiState.value.invite))
            call.enqueue(object : Callback<MessageData> {
                override fun onResponse(call: Call<MessageData>, response: Response<MessageData>) {
                    _uiState.update { it.copy(invite_message = response.body()!!.message!!) }
                    loadData()
                }

                override fun onFailure(call: Call<MessageData>, t: Throwable) {
                    //дописать
                }
            })
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
                    is_archive = 1
                )

                viewModelScope.launch {
                    val accessToken = dataStore.getAccessToken().first() ?: ""
                    val call: Call<ProjectCurrentResponse> = ApiClient().updateProject("Bearer " + accessToken, updateProject)
                    call.enqueue(object : Callback<ProjectCurrentResponse> {
                        override fun onResponse(call: Call<ProjectCurrentResponse>, response: Response<ProjectCurrentResponse>) {
                            openPanel("")
                            loadData()
                        }

                        override fun onFailure(call: Call<ProjectCurrentResponse>, t: Throwable) {
                            Log.d("tag", "response t" + t.message)
                        }
                    })
                }
            }
        }
    }

    fun addProject(){
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            val call: Call<ProjectDataResponse> = ApiClient().postNewProject("Bearer " + accessToken, ProjectData(uiState.value.newProjectName, uiState.value.newProjectDescription))
            call.enqueue(object : Callback<ProjectDataResponse> {
                override fun onResponse(call: Call<ProjectDataResponse>, response: Response<ProjectDataResponse>) {
                    notAddingProjectYet()
                    loadData()
                }

                override fun onFailure(call: Call<ProjectDataResponse>, t: Throwable) {
                    //дописать
                }
            })
        }
    }

    fun loadData() {
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            val call: Call<List<ProjectDataResponse>> = ApiClient().getPersonalProject("Bearer " + accessToken)
            call.enqueue(object : Callback<List<ProjectDataResponse>> {
                override fun onResponse(call: Call<List<ProjectDataResponse>>, response: Response<List<ProjectDataResponse>>) {
                    val validData: MutableList<ProjectDataResponse> = mutableListOf()
                    response.body()?.forEach { item ->
                        if (item.is_personal && item.is_archive==0) {
                            validData.add(item)
                        }
                    }
                    _uiState.update { it.copy(personalProject = validData)}
                    _uiState.update { it.copy(searchPersonalProject = validData)}
                }

                override fun onFailure(call: Call<List<ProjectDataResponse>>, t: Throwable) {
                    //дописать
                }
            })
            val call1: Call<List<ProjectDataResponse>> = ApiClient().getGroupProject("Bearer " + accessToken)
            call1.enqueue(object : Callback<List<ProjectDataResponse>> {
                override fun onResponse(call1: Call<List<ProjectDataResponse>>, response: Response<List<ProjectDataResponse>>) {
                    val validData: MutableList<ProjectDataResponse> = mutableListOf()
                    response.body()?.forEach { item ->
                        if (!item.is_personal && item.is_archive==0) {
                            validData.add(item)
                        }
                    }
                    _uiState.update { it.copy(groupProject = validData)}
                    _uiState.update { it.copy(searchGroupProject = validData)}

                }

                override fun onFailure(call1: Call<List<ProjectDataResponse>>, t: Throwable) {
                    //дописать
                }
            })
        }
    }
}