package com.coolgirl.majko.Screen.Register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.coolgirl.majko.commons.ApiError
import com.coolgirl.majko.commons.ApiExeption
import com.coolgirl.majko.commons.ApiSuccess
import com.coolgirl.majko.data.MajkoUserRepository
import com.coolgirl.majko.data.dataStore.UserDataStore
import com.coolgirl.majko.data.remote.dto.UserSignUpData.UserSignUpData
import com.coolgirl.majko.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(private val dataStore : UserDataStore, private val majkoRepository: MajkoUserRepository) : ViewModel() {

    private val _uiState =  MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun updateUserName(username : String) {
        _uiState.update { it.copy(userName = username) }
    }
    fun updateUserPassword(password: String) {
        _uiState.update { it.copy(userPassword = password) }
    }
    fun updateUserPasswordRepeat(passwordRepeat: String) {
        _uiState.update { it.copy(userPasswordRepeat = passwordRepeat) }
    }
    fun updateUserLogin(login : String) {
        _uiState.update { it.copy(userLogin = login) }
    }

    fun setAccesToken(token : String){
        viewModelScope.launch {
            dataStore.setAccesToken(token)
        }
    }

    fun signUp(navController: NavController){
        viewModelScope.launch {
            majkoRepository.signUp(UserSignUpData(uiState.value.userLogin, uiState.value.userPassword, uiState.value.userName))
                .collect() { response ->
                    when (response) {
                        is ApiSuccess -> {
                            setAccesToken(response.data!!.accessToken!!)
                            navController.navigate(Screen.Task.route) {
                                launchSingleTop = true
                                popUpTo(navController.graph.id) {
                                    inclusive = true
                                }
                            }
                        }
                        is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                        is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                    }
                }
        }
    }

}