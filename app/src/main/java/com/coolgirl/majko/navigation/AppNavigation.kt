package com.coolgirl.majko.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(val route: String) {

    object Login : Screen("login")

    object Register : Screen("register")

    object Task : Screen("tasks")

    object Group : Screen("group")

    object Profile : Screen("profile")

    object Project : Screen("project")

    object Archive : Screen("archive")

    object Splash : Screen("splash")

    object ProjectEditor : Screen("projectEditor/{${NavArgs.PROJECT_ID}}")
    object GroupEditor : Screen("groupEditor/{${NavArgs.GROUP_ID}}")
    object TaskEditor : Screen("taskEditor/{${NavArgs.TASK_ID}}")

    fun createRoute(vararg args: String): String {
        return buildString {
            var currentRoute = route
            args.forEach { arg ->
                val placeholder = currentRoute.substringBefore('}').substringAfterLast('{')
                currentRoute = currentRoute.replaceFirst("{$placeholder}", arg)
            }
            append(currentRoute)
        }
    }

    fun createStringArgument(name: String) = navArgument(name) {
        type = NavType.StringType
    }

    object NavArgs {
        const val PROJECT_ID = "projectId"
        const val GROUP_ID = "groupId"
        const val TASK_ID = "taskId"
    }

}