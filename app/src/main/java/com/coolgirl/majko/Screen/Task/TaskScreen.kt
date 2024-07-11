package com.coolgirl.majko.Screen.Task

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.coolgirl.majko.navigation.BottomBar
import com.coolgirl.majko.navigation.BottomBarScreens

@Composable
fun TaskScreen(navController: NavHostController){
    val viewModel:TaskViewModel = viewModel()
    SetTaskScreen(navController, viewModel)
}

@Composable
fun SetTaskScreen(navController: NavHostController, viewModel: TaskViewModel){
    Column(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.94f)) {
            Text(text = "Задачи", fontWeight = FontWeight.Bold, fontSize = 30.sp)
        }
        BottomBar(navController, listOf(
            BottomBarScreens.Notifications,
            BottomBarScreens.Task,
            BottomBarScreens.Profile
        ))
    }

}