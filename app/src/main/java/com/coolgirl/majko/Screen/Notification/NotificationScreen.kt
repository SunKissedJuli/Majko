package com.coolgirl.majko.Screen.Notification

import android.app.Notification
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.coolgirl.majko.navigation.BottomBar
import com.coolgirl.majko.navigation.BottomBarScreens

@Composable
fun NotificationScreen(navController: NavHostController) {
    Column(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.94f)) {
            Text(text = "Уведомления", fontWeight = FontWeight.Bold, fontSize = 30.sp)
        }
        BottomBar(navController, listOf(
            BottomBarScreens.Notifications,
            BottomBarScreens.Task,
            BottomBarScreens.Profile
        ))
    }
}