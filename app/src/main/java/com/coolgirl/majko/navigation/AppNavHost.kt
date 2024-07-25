package com.coolgirl.majko.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.coolgirl.majko.Sc.GroupEditorScreen
import com.coolgirl.majko.Screen.Archive.ArchiveScreen
import com.coolgirl.majko.Screen.Group.GroupScreen
import com.coolgirl.majko.Screen.Login.LoginScreen
import com.coolgirl.majko.Screen.Profile.ProfileScreen
import com.coolgirl.majko.Screen.Project.ProjectScreen
import com.coolgirl.majko.Screen.ProjectEdit.ProjectEditScreen
import com.coolgirl.majko.Screen.Register.RegisterScreen
import com.coolgirl.majko.Screen.Task.TaskScreen
import com.coolgirl.majko.Screen.TaskEditor.TaskEditorScreen
import com.coolgirl.majko.navigation.Screen.Archive.createStringArgument

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

        composable(Screen.Group.route) {
            GroupScreen(navController)
        }

        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }

        composable(Screen.Project.route) {
            ProjectScreen(navController)
        }

        composable(Screen.Archive.route) {
            ArchiveScreen(navController)
        }

        composable(Screen.Register.route) {
            RegisterScreen(navController)
        }

        composable(
            route = Screen.ProjectEditor.route,
            arguments = listOf(createStringArgument(Screen.NavArgs.PROJECT_ID))
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString(Screen.NavArgs.PROJECT_ID) ?: ""
            ProjectEditScreen(navController, projectId)
        }

        composable(
            route = Screen.GroupEditor.route,
            arguments = listOf(createStringArgument(Screen.NavArgs.GROUP_ID))
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString(Screen.NavArgs.GROUP_ID) ?: ""
            GroupEditorScreen(navController, groupId)
        }

        composable(
            route = Screen.TaskEditor.route,
            arguments = listOf(createStringArgument(Screen.NavArgs.TASK_ID))
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString(Screen.NavArgs.TASK_ID) ?: ""
            TaskEditorScreen(navController, taskId)
        }


    }
}