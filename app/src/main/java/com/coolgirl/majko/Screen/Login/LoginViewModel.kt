package com.coolgirl.majko.Screen.Login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.coolgirl.majko.R
import com.coolgirl.majko.data.dataStore.UserDataStore
import com.coolgirl.majko.data.remote.dto.UserSignInData
import com.coolgirl.majko.data.remote.dto.UserSignInDataResponse
import com.coolgirl.majko.data.remote.dto.UserSignUpData.UserSignUpData
import com.coolgirl.majko.data.remote.dto.UserSignUpData.UserSignUpDataResponse
import com.coolgirl.majko.di.ApiClient
import com.coolgirl.majko.navigation.Screen
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class LoginViewModel(userdataStore : UserDataStore) : ViewModel() {
    val dataStore : UserDataStore = userdataStore
    private var isThisSignIn : Boolean = true
    var userName by mutableStateOf("")
    var userLogin by mutableStateOf("")
    var userPassword by mutableStateOf("")
    var userPasswordRepeat by mutableStateOf("")
    var change by mutableStateOf("")

    fun updateUserName(username : String) {
        userName = username
    }
    fun updateUserPassword(password: String) {
        userPassword = password
    }
    fun updateUserPasswordRepeat(passwordRepeat: String) {
        userPasswordRepeat = passwordRepeat
    }
    fun updateUserLogin(login : String) {
        userLogin= login
    }
    fun isThisSignIn() : Boolean{
        return isThisSignIn
    }
    fun changeScreen(){
        isThisSignIn = !isThisSignIn
        change = RandomString()
    }
    fun bottomText() : Int{
        if(isThisSignIn)
            return  R.string.login_registrationoffer
        else
            return R.string.login_enteroffer
    }
    fun enterButtonText() : Int{
        if(isThisSignIn)
            return R.string.login_enter
        else
            return  R.string.login_registration
    }

    fun setAccesToken(token : String){
        viewModelScope.launch {
            dataStore.setAccesToken(token)
        }
    }

    fun signIn(navController: NavController){
        if(isThisSignIn){
            if(!userPassword.equals("")&&!userLogin.equals("")){
                val call: Call<UserSignInDataResponse> = ApiClient().signIn(UserSignInData(userLogin, userPassword))
                call.enqueue(object : Callback<UserSignInDataResponse> {
                    override fun onResponse(call: Call<UserSignInDataResponse>, response: Response<UserSignInDataResponse>) {
                        setAccesToken(response.body()!!.accessToken!!)
                        navController.navigate(Screen.Task.route){
                            launchSingleTop = true
                            popUpTo(navController.graph.id){
                                inclusive = true
                            }
                        }
                    }

                    override fun onFailure(call: Call<UserSignInDataResponse>, t: Throwable) {
                        //дописать
                    } })
            }
        }else{
            if(!userPassword.equals("")&&!userLogin.equals("")&&!userName.equals("")){
                val call: Call<UserSignUpDataResponse> = ApiClient().signUp(UserSignUpData(userLogin, userPassword, userName))
                call.enqueue(object : Callback<UserSignUpDataResponse> {
                    override fun onResponse(call: Call<UserSignUpDataResponse>, response: Response<UserSignUpDataResponse>) {
                        setAccesToken(response.body()!!.accessToken!!)
                        navController.navigate(Screen.Task.route){
                            launchSingleTop = true
                            popUpTo(navController.graph.id){
                                inclusive = true
                            }
                        }
                    }

                    override fun onFailure(call: Call<UserSignUpDataResponse>, t: Throwable) {
                        //дописать
                    }
                })
            }
        }
    }

    fun RandomString() : String{
        val length = Random.nextInt(2, 4 + 1)
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..length)
            .map { charPool[Random.nextInt(0, charPool.size)] }
            .joinToString("")
    }

}