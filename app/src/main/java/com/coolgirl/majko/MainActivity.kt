package com.coolgirl.majko

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.coolgirl.majko.Screen.Profile.ProfileViewModel
import com.coolgirl.majko.data.dataStore.UserDataStore
import com.coolgirl.majko.navigation.AppNavHost
import com.coolgirl.majko.navigation.BottomBar
import com.coolgirl.majko.navigation.BottomBarScreens
import com.coolgirl.majko.navigation.Screen
import com.coolgirl.majko.ui.theme.MajkoTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MajkoTheme {

                val dataStore: UserDataStore by inject()
                val navController = rememberNavController()
                val coroutineScope = rememberCoroutineScope()
                var accessToken by remember { mutableStateOf<String?>(null) }
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = currentBackStackEntry?.destination

                LaunchedEffect(Unit) {
                    coroutineScope.launch {
                        accessToken = dataStore.getAccessToken().first()
                        Log.d("tag", "access = " + accessToken)
                    }
                }

                val startDestination = if (!accessToken.isNullOrEmpty()) {
                    Screen.Profile.route }
                else {
                    Screen.Login.route
                }

                Scaffold(
                    bottomBar = {
                        if (currentDestination != null) {
                            if(currentDestination.route!=Screen.Login.route&&currentDestination.route!=Screen.Register.route
                                &&currentDestination.route!=Screen.TaskEditor.createRoute()
                                &&currentDestination.route!=Screen.GroupEditor.createRoute()
                                &&currentDestination.route!=Screen.ProjectEditor.createRoute()){

                                    BottomBar(navController,
                                        listOf(BottomBarScreens.Group,
                                            BottomBarScreens.Project,
                                            BottomBarScreens.Task,
                                            BottomBarScreens.Archive,
                                            BottomBarScreens.Profile))
                            }
                        }
                    }) {
                    Box(Modifier.padding(it)){
                        AppNavHost(navController, startDestination)
                    }
                }
            }
        }
    }
}