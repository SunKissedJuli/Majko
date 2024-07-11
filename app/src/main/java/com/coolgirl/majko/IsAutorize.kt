package com.coolgirl.majko

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.coolgirl.majko.navigation.AppNavHost
import com.coolgirl.majko.navigation.Screen

@Composable
fun IsAutorize(){
    AppNavHost(navController = rememberNavController(), startDestination = Screen.Login.route)
}