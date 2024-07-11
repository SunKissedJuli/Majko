package com.coolgirl.majko.navigation

import com.coolgirl.majko.R

sealed class BottomBarScreens(
    val route: String,
    val title: String,
    val icon: Int
) {

    object Notifications : BottomBarScreens(
        route = "notifications",
        title = "Уведомления",
        icon = R.drawable.icon_notifications
    )

    object Task : BottomBarScreens(
        route = "tasks",
        title = "Мои задачи",
        icon = R.drawable.icon_task
    )

    object Profile : BottomBarScreens(
        route = "profile",
        title = "Профиль",
        icon = R.drawable.icon_profile
    )
}