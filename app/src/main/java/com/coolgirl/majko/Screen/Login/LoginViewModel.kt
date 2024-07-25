package com.coolgirl.majko.Screen.Login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.coolgirl.majko.commons.ApiError
import com.coolgirl.majko.commons.ApiExeption
import com.coolgirl.majko.commons.ApiSuccess
import com.coolgirl.majko.data.MajkoRepository
import com.coolgirl.majko.data.dataStore.UserDataStore
import com.coolgirl.majko.data.remote.dto.UserSignInData
import com.coolgirl.majko.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(private val dataStore : UserDataStore, private val majkoRepository: MajkoRepository) : ViewModel() {
    private val _uiState =  MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun updateUserPassword(password: String) {
        _uiState.update { it.copy(userPassword = password) }
    }

    fun updateUserLogin(login : String) {
        _uiState.update { it.copy(userLogin = login) }
    }

    fun setAccesToken(token : String){
        viewModelScope.launch {
            dataStore.setAccesToken(token)
        }
    }

    fun signIn(navController: NavController){
        if(!uiState.value.userPassword.equals("")&&!uiState.value.userLogin.equals("")){
            viewModelScope.launch {
                majkoRepository.signIn(UserSignInData(uiState.value.userLogin, uiState.value.userPassword))
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
}