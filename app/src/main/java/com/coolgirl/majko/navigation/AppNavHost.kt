package com.coolgirl.majko.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.coolgirl.majko.Screen.Login.LoginScreen
import com.coolgirl.majko.Screen.Notification.NotificationScreen
import com.coolgirl.majko.Screen.Profile.ProfileScreen
import com.coolgirl.majko.Screen.Task.TaskScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String = Screen.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }

        composable(Screen.Task.route) {
            TaskScreen(navController)
        }

        composable(Screen.Notification.route) {
            NotificationScreen(navController)
        }

        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }
    }
}