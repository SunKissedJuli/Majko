package com.coolgirl.majko.navigation

import androidx.compose.ui.res.stringResource
import com.coolgirl.majko.R

sealed class BottomBarScreens(
    val route: String,
    val title: Int,
    val icon: Int
) {

    object Project : BottomBarScreens(
        route = "project",
        title = R.string.bottomBar_project,
        icon = R.drawable.icon_project
    )

    object Task : BottomBarScreens(
        route = "tasks",
        title = R.string.bottomBar_task,
        icon = R.drawable.icon_task
    )

    object Profile : BottomBarScreens(
        route = "profile",
        title = R.string.bottomBar_profile,
        icon = R.drawable.icon_profile
    )

    object Archive : BottomBarScreens(
        route = "archive",
        title = R.string.bottomBar_archive,
        icon = R.drawable.icon_archive
    )

    object Group : BottomBarScreens(
        route = "group",
        title = R.string.bottomBar_group,
        icon = R.drawable.icon_group
    )
}