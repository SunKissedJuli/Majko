package com.coolgirl.majko.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")

    object Task : Screen("tasks")
    object Notification : Screen("notification")
    object Profile : Screen("profile")
}