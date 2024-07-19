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
import com.coolgirl.majko.Screen.Task.TaskScreen
import com.coolgirl.majko.Screen.TaskEditor.TaskEditorScreen

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

        composable(Screen.ProjectEditor.route,
            arguments = listOf(navArgument("project_id"){
                type = NavType.StringType
            })){
            val project_id : String = it.arguments?.getString("project_id")!!
            ProjectEditScreen(navController, project_id)
        }

        composable(Screen.GroupEditor.route,
            arguments = listOf(navArgument("group_id"){
                type = NavType.StringType
            })){
            val group_id : String = it.arguments?.getString("group_id")!!
            GroupEditorScreen(navController, group_id)
        }

        composable(Screen.TaskEditor.route,
            arguments = listOf(navArgument("task_id"){
                type = NavType.StringType
            })){
            val task_id : String = it.arguments?.getString("task_id")!!
            TaskEditorScreen(navController, task_id)
        }
    }
}