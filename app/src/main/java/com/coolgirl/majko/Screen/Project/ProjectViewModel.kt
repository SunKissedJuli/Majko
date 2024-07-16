package com.coolgirl.majko.Screen.Project

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coolgirl.majko.data.dataStore.UserDataStore
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectData
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectDataResponse
import com.coolgirl.majko.di.ApiClient
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProjectViewModel(private val dataStore : UserDataStore) : ViewModel(){
    private val _uiState = MutableStateFlow(ProjectUiState())
    val uiState: StateFlow<ProjectUiState> = _uiState.asStateFlow()

    init{loadData()}

    fun updateProjectName(name: String){
        _uiState.update { it.copy(newProjectName = name) }
    }

    fun updateProjectDescription(description: String){
        _uiState.update { it.copy(newProjectDescription = description) }
    }

    fun addingProject(){
        _uiState.update { it.copy(is_adding_background = 0.5f)}
        _uiState.update { it.copy(is_adding = true)}
    }

    fun notAddingProjectYet(){
        _uiState.update { it.copy(is_adding_background = 1f)}
        _uiState.update { it.copy(is_adding = false)}
    }

    fun addProject(){
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            val call: Call<ProjectDataResponse> = ApiClient().postNewProject("Bearer " + accessToken, ProjectData(uiState.value.newProjectName, uiState.value.newProjectDescription))
            call.enqueue(object : Callback<ProjectDataResponse> {
                override fun onResponse(call: Call<ProjectDataResponse>, response: Response<ProjectDataResponse>) {
                    if (response.code() == 200 || response.code() == 201) {
                        notAddingProjectYet()
                        loadData()
                        Log.d("tag", "Projectlog addProject body = " + response.body())
                    }
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
                    if (response.code() == 200 && response.body()!=null) {
                        Log.d("tag", "Projectlog groupProject body = " + response.body())
                        val validData: MutableList<ProjectDataResponse> = mutableListOf()
                        response.body()?.forEach { item ->
                            if (item.is_personal && item.is_archive==0) {
                                validData.add(item)
                            }
                        }
                          _uiState.update { it.copy(personalProject = validData)}
                       // _uiState.update { it.copy(personalProject = response.body())}
                        Log.d("tag", "Projectlog personalProject = " + uiState.value.personalProject)
                    }
                }

                override fun onFailure(call: Call<List<ProjectDataResponse>>, t: Throwable) {
                    //дописать
                }
            })
            val call1: Call<List<ProjectDataResponse>> = ApiClient().getGroupProject("Bearer " + accessToken)
            call1.enqueue(object : Callback<List<ProjectDataResponse>> {
                override fun onResponse(call1: Call<List<ProjectDataResponse>>, response: Response<List<ProjectDataResponse>>) {
                    if (response.code() == 200 && response.body()!=null) {
                        val validData: MutableList<ProjectDataResponse> = mutableListOf()
                        Log.d("tag", "Projectlog groupProject body = " + response.body())
                        response.body()?.forEach { item ->
                            if (!item.is_personal && item.is_archive==0) {
                                validData.add(item)
                            }
                        }
                        _uiState.update { it.copy(groupProject = validData)}
                     //   _uiState.update { it.copy(groupProject = response.body())}
                        Log.d("tag", "Projectlog groupProject = " + uiState.value.groupProject)
                    }
                }

                override fun onFailure(call1: Call<List<ProjectDataResponse>>, t: Throwable) {
                    //дописать
                }
            })
        }
    }
}