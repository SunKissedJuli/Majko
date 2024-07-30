package com.coolgirl.majko.Screen.Register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.coolgirl.majko.R
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

    fun isError(message: Int?){
        if(uiState.value.isError){
            _uiState.update { it.copy(isError = false)}
        }else{
            _uiState.update { it.copy(errorMessage = message)}
            _uiState.update { it.copy(isError = true)}
        }
    }

    fun signUp(navController: NavController) {
        if (uiState.value.userPassword.isNullOrEmpty() || uiState.value.userLogin.isNullOrEmpty()
            || uiState.value.userName.isNullOrEmpty() || uiState.value.userPasswordRepeat.isNullOrEmpty()) {
            isError(R.string.error_moredata)
        }else if(!uiState.value.userPassword.equals(uiState.value.userPasswordRepeat)){
            isError(R.string.error_login_passwordsnotsame)
        }else if(uiState.value.userPassword.length<8){
            isError(R.string.error_login_passwordlenght)
        } else{
            viewModelScope.launch {
                majkoRepository.signUp(
                    UserSignUpData(uiState.value.userLogin, uiState.value.userPassword, uiState.value.userName)).collect() { response ->
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
                            is ApiError -> {
                                isError(R.string.error_register)
                                Log.d("TAG", "error message = " + response.message)
                            }
                            is ApiExeption -> {
                                isError(R.string.error_register)
                                Log.d("TAG", "exeption e = " + response.e)
                            }
                        }
                    }
            }
        }
    }
}