package com.coolgirl.majko.Screen.Profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.coolgirl.majko.navigation.BottomBar
import com.coolgirl.majko.navigation.BottomBarScreens
import com.coolgirl.majko.R

@Composable
fun ProfileScreen( navController: NavHostController) {
    val viewModel: ProfileViewModel = viewModel(key = "profileViewModel",
        factory = ProfileViewModelProvider.getInstance(LocalContext.current))

    val uiState by viewModel.uiState.collectAsState()
    Column(
        Modifier
            .fillMaxSize()
            .background(colorResource(R.color.white))) {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.93f)) {
            SetProfileScreen(uiState, {viewModel.updateUserName(it)}, {viewModel.updateUserEmail(it)})
        }
        BottomBar(navController, listOf(
            BottomBarScreens.Notifications,
            BottomBarScreens.Task,
            BottomBarScreens.Profile))
    }
}

@Composable
fun SetProfileScreen(uiState: ProfileUiState, onUpdateUserName: (String) -> Unit, onUpdateUserEmail: (String) -> Unit) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.icon_plug),
            contentDescription = "image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clickable {}
                .size(200.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Имя:", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.width(20.dp))
            TextField(
                value = uiState.userName,
                modifier = Modifier
                    .height(55.dp)
                    .width(150.dp),
                onValueChange = onUpdateUserName,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorResource(R.color.white),
                    unfocusedContainerColor = colorResource(R.color.white)
                )
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Логин:", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.width(20.dp))
            TextField(
                value = uiState.userEmail,
                modifier = Modifier
                    .height(55.dp)
                    .width(150.dp),
                onValueChange = onUpdateUserEmail,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorResource(R.color.white),
                    unfocusedContainerColor = colorResource(R.color.white)
                )
            )
        }
    }
}
