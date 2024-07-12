package com.coolgirl.majko.commons

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.rememberNavController
import com.coolgirl.majko.data.dataStore.UserDataStore
import com.coolgirl.majko.navigation.AppNavHost
import com.coolgirl.majko.navigation.Screen
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun IsAutorize(){
    val dataStore : UserDataStore = UserDataStore(LocalContext.current)
    val coroutineScope = rememberCoroutineScope()
    var accessToken by remember { mutableStateOf("") }

    coroutineScope.launch {
        accessToken = dataStore.getAccessToken().first()?: ""
    }
    if(!accessToken.equals("")){
        AppNavHost(navController = rememberNavController(), startDestination = Screen.Profile.route)
    }else{
        AppNavHost(navController = rememberNavController(), startDestination = Screen.Login.route)
    }

}

fun RandomString() : String{
    val length = Random.nextInt(2, 4 + 1)
    val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return (1..length)
        .map { charPool[Random.nextInt(0, charPool.size)] }
        .joinToString("")
}

enum class LoadNotesStatus {
    NOT_STARTED,
    COMPLETED
}




