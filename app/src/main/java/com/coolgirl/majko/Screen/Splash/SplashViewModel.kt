package com.coolgirl.majko.Screen.Splash

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.coolgirl.majko.data.dataStore.UserDataStore
import com.coolgirl.majko.navigation.Screen
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SplashViewModel() : ViewModel(), KoinComponent {
    private val dataStore: UserDataStore by inject()
    private val accessToken = mutableStateOf<String?>(null)

    fun IsAutorize(navController: NavController){
       viewModelScope.launch {
           accessToken.value = dataStore.getAccessToken().first()

           val startDestination = if (!accessToken.value.isNullOrEmpty()) {
               Screen.Profile.route
           } else {
               Screen.Login.route
           }

           navController.navigate(startDestination)
       }
    }
}