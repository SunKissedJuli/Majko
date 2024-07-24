package com.coolgirl.majko.commons

import android.app.Service
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.coolgirl.majko.Screen.Profile.ProfileViewModel
import com.coolgirl.majko.data.dataStore.UserDataStore
import com.coolgirl.majko.navigation.AppNavHost
import com.coolgirl.majko.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.*
import org.koin.androidx.viewmodel.ext.android.viewModel

sealed interface ApiResult<T:Any>
class ApiSuccess<T:Any>(val data: T): ApiResult<T>
class ApiError<T:Any>(val code: Int, val message: String?): ApiResult<T>
class ApiExeption<T:Any>(val e: Throwable): ApiResult<T>

abstract class MajcoViewModels : Service() {

}

/*@Composable
fun IsAutorize(){
    val dataStore : UserDataStore = UserDataStore(LocalContext.current)
    val coroutineScope = rememberCoroutineScope()
    var accessToken by remember { mutableStateOf("") }

    coroutineScope.launch {
        accessToken = dataStore.getAccessToken().first()?: ""
    }
    if(accessToken != ""){
        AppNavHost(navController = rememberNavController(), startDestination = Screen.Profile.route)
    }else{
        AppNavHost(navController = rememberNavController(), startDestination = Screen.Login.route)
    }
}*/








