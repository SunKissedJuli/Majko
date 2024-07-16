package com.coolgirl.majko.navigation

import com.coolgirl.majko.R

class ModalNavigationDrawer {
}

sealed class ModalNavigationDrawerScreens(
    val route: String,
    val title: String
) {

    object Task : ModalNavigationDrawerScreens(
        route = "tasks",
        title = "Мои задачи"
    )

    object Project : ModalNavigationDrawerScreens(
        route = "project",
        title = "Мои проекты"
    )

    object Profile : ModalNavigationDrawerScreens(
        route = "profile",
        title = "Профиль"
    )
}