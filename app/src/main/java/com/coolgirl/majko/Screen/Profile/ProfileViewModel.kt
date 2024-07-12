package com.coolgirl.majko.Screen.Profile

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coolgirl.majko.commons.RandomString
import com.coolgirl.majko.data.dataStore.UserDataStore
import com.coolgirl.majko.data.remote.dto.CurrentUserData.CurrentUserDataResponse
import com.coolgirl.majko.di.ApiClient
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(userdataStore : UserDataStore) : ViewModel() {
    val dataStore : UserDataStore = userdataStore
    var acessToken by mutableStateOf("")
    var userName by mutableStateOf("")
    var change by mutableStateOf("")
    var dataLoaded by mutableStateOf("")
    var userEmail by mutableStateOf("")


    fun updateUserName(username : String) {
        userName = username
        Log.d("tag", "profileScreen updateUserEmail userName = " + userName)
    }
    fun updateUserEmail(email: String){
        userEmail = email
        Log.d("tag", "profileScreen updateUserEmail userEmail = " + userEmail)
    }
    fun getAccessToken() : String{
        viewModelScope.launch {
            acessToken = dataStore.getAccessToken().first()?: ""
        }
        return acessToken
    }

    fun changeScreen(){
        change = RandomString()
    }

    fun LoadData(){
        acessToken = getAccessToken()
        val call: Call<CurrentUserDataResponse> = ApiClient().currentUser("Bearer " + acessToken)
        call.enqueue(object : Callback<CurrentUserDataResponse> {
            override fun onResponse(call: Call<CurrentUserDataResponse>, response: Response<CurrentUserDataResponse>) {
                if(response.code()==200){
                    updateUserEmail(response.body()?.email.toString())
                    updateUserName(response.body()?.name.toString())
                    Log.d("tag", "profileScreen LoadData username = " + userName)
                    changeScreen()
                    dataLoaded = RandomString()
                }
            }
            override fun onFailure(call: Call<CurrentUserDataResponse>, t: Throwable) {
                //дописать
            } })
    }
}