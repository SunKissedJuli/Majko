package com.coolgirl.majko.Screen.Profile

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.coolgirl.majko.navigation.BottomBar
import com.coolgirl.majko.navigation.BottomBarScreens

import com.coolgirl.majko.R
import com.coolgirl.majko.commons.LoadNotesStatus
import com.coolgirl.majko.data.dataStore.UserDataStore
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(navController: NavHostController) {
    val dataStore : UserDataStore = UserDataStore(LocalContext.current)
    val viewModel: ProfileViewModel = ProfileViewModel(dataStore)
    val coroutineScope = rememberCoroutineScope()
    var loadNotesStatus by remember { mutableStateOf(LoadNotesStatus.NOT_STARTED) }

    LaunchedEffect(loadNotesStatus) {
        coroutineScope.launch {
            viewModel.LoadData()
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(colorResource(R.color.white))) {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.94f)) {

            SetProfileScreen(navController, viewModel)
        }
        BottomBar(navController, listOf(
            BottomBarScreens.Notifications,
            BottomBarScreens.Task,
            BottomBarScreens.Profile
        ))
    }
}

@Composable
fun SetProfileScreen(navController: NavHostController, viewModel: ProfileViewModel){
    Column(Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center) {
        Image(painter = painterResource(R.drawable.icon_plug),
            contentDescription = "image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clickable {}
                .size(200.dp)
                .clip(CircleShape))
        Spacer(modifier = Modifier.height(20.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Имя:", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.width(20.dp))
            TextField(
                value = viewModel.userName,
                modifier = Modifier
                    .height(55.dp)
                    .width(150.dp),
                onValueChange = { viewModel.updateUserName(it) },
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
                value = viewModel.userEmail,
                modifier = Modifier
                    .height(55.dp)
                    .width(150.dp),
                onValueChange = { viewModel.updateUserEmail(it) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorResource(R.color.white),
                    unfocusedContainerColor = colorResource(R.color.white)))
        }

    }
}