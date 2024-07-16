package com.coolgirl.majko.navigation


sealed class ModalNavigationDrawerScreens(
    val route: String,
    val title: String
) {

    object Task : ModalNavigationDrawerScreens(
        route = "tasks",
        title = "Задачи"
    )

    object Project : ModalNavigationDrawerScreens(
        route = "project",
        title = "Проекты"
    )

    object Profile : ModalNavigationDrawerScreens(
        route = "profile",
        title = "Профиль"
    )

    object Archive : ModalNavigationDrawerScreens(
        route = "archive",
        title = "Архив"
    )
}