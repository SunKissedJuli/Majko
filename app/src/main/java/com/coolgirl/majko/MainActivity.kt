package com.coolgirl.majko

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.coolgirl.majko.data.dataStore.UserDataStore
import com.coolgirl.majko.navigation.AppNavHost
import com.coolgirl.majko.navigation.BottomBar
import com.coolgirl.majko.navigation.BottomBarScreens
import com.coolgirl.majko.navigation.Screen
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val dataStore: UserDataStore = UserDataStore(LocalContext.current)
            val coroutineScope = rememberCoroutineScope()
            var accessToken by remember { mutableStateOf("") }

            LaunchedEffect(Unit) {
                coroutineScope.launch {
                    accessToken = dataStore.getAccessToken().first() ?: ""
                }
            }

            val startDestination = if (accessToken.isNotEmpty()) {
                Screen.Profile.route
            } else {
                Screen.Login.route
            }

            val currentBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = currentBackStackEntry?.destination

            Scaffold(
                bottomBar = {
                    if (currentDestination != null) {
                        if(currentDestination.route!="login"){
                            Column(Modifier.fillMaxHeight(0.07f)) {
                                BottomBar(navController, listOf(BottomBarScreens.Notifications, BottomBarScreens.Task, BottomBarScreens.Profile))
                            }
                        }
                    }

                }
            ) {
                AppNavHost(navController, startDestination)
            }
        }
    }
}