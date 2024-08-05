package com.coolgirl.majko.Screen.Login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.coolgirl.majko.R
import com.coolgirl.majko.data.remote.ApiError
import com.coolgirl.majko.data.remote.ApiExeption
import com.coolgirl.majko.data.remote.ApiSuccess
import com.coolgirl.majko.data.MajkoUserRepository
import com.coolgirl.majko.data.dataStore.UserDataStore
import com.coolgirl.majko.data.remote.dto.UserSignInData
import com.coolgirl.majko.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LoginViewModel(private val majkoRepository: MajkoUserRepository) : ViewModel(), KoinComponent {

    private val dataStore: UserDataStore by inject()
    private val _uiState =  MutableStateFlow(LoginUiState.default())
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

    fun isError(message: Int?){
        if(uiState.value.isError){
            _uiState.update { it.copy(isError = false)}
        }else{
            _uiState.update { it.copy(errorMessage = message, isError = true)}
        }
    }

    fun signIn(navController: NavController){
        if(!uiState.value.userPassword.isNullOrEmpty()&&!uiState.value.userLogin.isNullOrEmpty()){
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
                            is ApiError -> {
                                if (response.code == 422) {
                                    isError(R.string.error_login_datanotfound) }
                                else { isError(R.string.error_login) }
                            }
                            is ApiExeption -> {
                                isError(R.string.error_login)
                            }
                        }
                    }
            }
        }
        else{
            isError(R.string.error_moredata)
        }
    }
}