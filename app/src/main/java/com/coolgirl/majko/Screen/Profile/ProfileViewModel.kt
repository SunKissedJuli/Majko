package com.coolgirl.majko.Screen.Profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coolgirl.majko.data.dataStore.UserDataStore
import com.coolgirl.majko.data.remote.dto.CurrentUserDataResponse
import com.coolgirl.majko.data.remote.dto.UserUpdateEmail
import com.coolgirl.majko.data.remote.dto.UserUpdateName
import com.coolgirl.majko.data.remote.dto.UserUpdatePassword
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

    fun updateOldPassword(password: String) {
        _uiState.update { it.copy(oldPassword = password) }
    }

    fun updateNewPassword(password: String) {
        _uiState.update { it.copy(newPassword = password) }
    }

    fun updateConfirmPassword(password: String) {
        _uiState.update { it.copy(confirmPassword = password) }
    }

    fun updateNameData() {
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            val call: Call<CurrentUserDataResponse> = ApiClient().updateUserName("Bearer " + accessToken, UserUpdateName(uiState.value.userName))
            call.enqueue(object : Callback<CurrentUserDataResponse> {
                override fun onResponse(call: Call<CurrentUserDataResponse>, response: Response<CurrentUserDataResponse>) {
                    if(response.code()==200){
                    }
                }
                override fun onFailure(call: Call<CurrentUserDataResponse>, t: Throwable) {
                    //дописать
                } })
        }
    }

    fun updateEmailData() {
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            val call: Call<CurrentUserDataResponse> = ApiClient().updateUserEmail("Bearer " + accessToken, UserUpdateEmail(uiState.value.userName, uiState.value.userEmail))
            call.enqueue(object : Callback<CurrentUserDataResponse> {
                override fun onResponse(call: Call<CurrentUserDataResponse>, response: Response<CurrentUserDataResponse>) {
                    if(response.code()==200){
                    }
                }
                override fun onFailure(call: Call<CurrentUserDataResponse>, t: Throwable) {
                    //дописать
                } })
        }
    }

    fun changePasswordScreen(){
        if(uiState.value.is_adding){
            _uiState.update { it.copy(is_adding_background = 1f)}
            _uiState.update { it.copy(is_adding = false)}
        }else{
            _uiState.update { it.copy(is_adding_background = 0.5f)}
            _uiState.update { it.copy(is_adding = true)}
        }

    }

    fun forgetAccount(){
        viewModelScope.launch {
            dataStore.setAccesToken("")
        }
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

    fun changePassword(){
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            val call: Call<CurrentUserDataResponse> = ApiClient().updateUserPassword("Bearer " + accessToken, UserUpdatePassword(uiState.value.userName,
                uiState.value.newPassword,uiState.value.confirmPassword, uiState.value.oldPassword))
            call.enqueue(object : Callback<CurrentUserDataResponse> {
                override fun onResponse(call: Call<CurrentUserDataResponse>, response: Response<CurrentUserDataResponse>) {
                    if(response.code()==200){
                        changePasswordScreen()
                    }
                }
                override fun onFailure(call: Call<CurrentUserDataResponse>, t: Throwable) {
                    //дописать
                } })
        }
    }
}
