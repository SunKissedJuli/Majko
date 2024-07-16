package com.coolgirl.majko.Screen.Archive

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coolgirl.majko.Screen.Project.ProjectUiState
import com.coolgirl.majko.data.dataStore.UserDataStore
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectDataResponse
import com.coolgirl.majko.di.ApiClient
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArchiveViewModel(private val dataStore:UserDataStore) : ViewModel(){
    private val _uiState = MutableStateFlow(ArchiveUiState())
    val uiState: StateFlow<ArchiveUiState> = _uiState.asStateFlow()

    init{ loadData() }

    fun loadData(){
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            val call: Call<List<ProjectDataResponse>> = ApiClient().getPersonalProject("Bearer " + accessToken)
            call.enqueue(object : Callback<List<ProjectDataResponse>> {
                override fun onResponse(call: Call<List<ProjectDataResponse>>, response: Response<List<ProjectDataResponse>>) {
                    if (response.code() == 200 && response.body()!=null) {
                        val validData: MutableList<ProjectDataResponse> = mutableListOf()
                        response.body()?.forEach { item ->
                            if (item.is_personal && item.is_archive==1) {
                                validData.add(item)
                            }
                        }
                        _uiState.update { it.copy(personalProject = validData)}
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
                        response.body()?.forEach { item ->
                            if (!item.is_personal && item.is_archive==1) {
                                validData.add(item)
                            }
                        }
                        _uiState.update { it.copy(groupProject = validData)}

                    }
                }

                override fun onFailure(call1: Call<List<ProjectDataResponse>>, t: Throwable) {
                    //дописать
                }
            })
        }
    }
}