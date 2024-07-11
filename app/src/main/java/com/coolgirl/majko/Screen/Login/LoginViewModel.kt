package com.coolgirl.majko.Screen.Login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.coolgirl.majko.R
import com.coolgirl.majko.commons.RandomString
import com.coolgirl.majko.data.remote.dto.UserSignInData
import com.coolgirl.majko.data.remote.dto.UserSignInDataResponse
import com.coolgirl.majko.di.ApiClient
import com.coolgirl.majko.navigation.Screen
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    private var isThisSignIn : Boolean = true
    var userLogin by mutableStateOf("")
    var userPassword by mutableStateOf("")
    var userPasswordRepeat by mutableStateOf("")
    var change by mutableStateOf("")

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

    fun signIn(navController: NavController){
        if(isThisSignIn){
            if(!userPassword.equals("")&&!userLogin.equals("")){
                val call: Call<UserSignInDataResponse> = ApiClient().signIn(UserSignInData(userLogin, userPassword))
                call.enqueue(object : Callback<UserSignInDataResponse> {
                    override fun onResponse(call: Call<UserSignInDataResponse>, response: Response<UserSignInDataResponse>) {
                        if(response.code()==200){
                            navController.navigate(Screen.Task.route)
                        }
                    }
                    override fun onFailure(call: Call<UserSignInDataResponse>, t: Throwable) {
                        //дописать
                    } })
            }
        }
    }
}