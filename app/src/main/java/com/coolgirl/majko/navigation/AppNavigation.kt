package com.coolgirl.majko.navigation

sealed class Screen(val route: String) {

    object Login : Screen("login")

    object Task : Screen("tasks")

    object Notification : Screen("notification")

    object Profile : Screen("profile")

    object Project : Screen("project")

    object Archive : Screen("archive")

    object ProjectEditor : Screen("project_editor/{project_id}"){
        fun project_id(project_id: String): String{
            return "project_editor/$project_id"
        }
    }


    object TaskEditor : Screen("task_editor/{task_id}"){
        fun task_id(task_id: String): String{
            return "task_editor/$task_id"
        }
    }

}