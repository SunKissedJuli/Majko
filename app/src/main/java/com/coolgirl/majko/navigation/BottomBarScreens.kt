package com.coolgirl.majko.navigation

import com.coolgirl.majko.R

sealed class BottomBarScreens(
    val route: String,
    val title: String,
    val icon: Int
) {

    object Notifications : BottomBarScreens(
        route = "project",
        title = "Проекты",
        icon = R.drawable.icon_project
    )

    object Task : BottomBarScreens(
        route = "tasks",
        title = "Задачи",
        icon = R.drawable.icon_task
    )

    object Profile : BottomBarScreens(
        route = "profile",
        title = "Профиль",
        icon = R.drawable.icon_profile
    )
}