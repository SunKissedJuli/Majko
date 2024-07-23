package com.coolgirl.majko.Screen.Archive

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coolgirl.majko.components.ProjectCardUiState
import com.coolgirl.majko.data.dataStore.UserDataStore
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectCurrentResponse
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectDataResponse
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectUpdate
import com.coolgirl.majko.di.ApiClient
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArchiveViewModel(private val dataStore:UserDataStore) : ViewModel(){
    private val _uiState = MutableStateFlow(ArchiveUiState())
    val uiState: StateFlow<ArchiveUiState> = _uiState.asStateFlow()

    val _uiStateCard = MutableStateFlow(ProjectCardUiState())
    val uiStateCard: StateFlow<ProjectCardUiState> = _uiStateCard.asStateFlow()

    init{ loadData() }

    fun openPanel(id: String){
        Log.d("tag", "хуй = " + id)
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
                    is_archive = 0
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

    fun loadData(){
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            val call: Call<List<ProjectDataResponse>> = ApiClient().getPersonalProject("Bearer " + accessToken)
            call.enqueue(object : Callback<List<ProjectDataResponse>> {
                override fun onResponse(call: Call<List<ProjectDataResponse>>, response: Response<List<ProjectDataResponse>>) {
                    val validData: MutableList<ProjectDataResponse> = mutableListOf()
                    response.body()?.forEach { item ->
                        if (item.is_personal && item.is_archive==1) {
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
                        if (!item.is_personal && item.is_archive==1) {
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