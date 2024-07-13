package com.coolgirl.majko.Screen.Profile

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coolgirl.majko.commons.RandomString
import com.coolgirl.majko.data.dataStore.UserDataStore
import com.coolgirl.majko.data.remote.dto.CurrentUserDataResponse
import com.coolgirl.majko.di.ApiClient
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(private val dataStore: UserDataStore) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init { loadData() }

    fun updateUserName(username: String) {
        _uiState.update { it.copy(userName = username) }
    }

    fun updateUserEmail(email: String) {
        _uiState.update { it.copy(userEmail = email) }
    }

    private fun loadData() {
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            val call: Call<CurrentUserDataResponse> = ApiClient().currentUser("Bearer " + accessToken)
            call.enqueue(object : Callback<CurrentUserDataResponse> {
                override fun onResponse(call: Call<CurrentUserDataResponse>, response: Response<CurrentUserDataResponse>) {
                    if(response.code()==200){
                        updateUserEmail(response.body()?.email.toString())
                        updateUserName(response.body()?.name.toString())
                    }
                }
                override fun onFailure(call: Call<CurrentUserDataResponse>, t: Throwable) {
                    //дописать
                } })
        }
    }
}
