package com.coolgirl.majko.commons

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.coolgirl.majko.data.dataStore.UserDataStore
import com.coolgirl.majko.navigation.AppNavHost
import com.coolgirl.majko.navigation.Screen
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.random.Random
import com.coolgirl.majko.R
import com.coolgirl.majko.data.remote.dto.FavoritesDataResponse
import com.coolgirl.majko.data.remote.dto.TaskData.TaskDataResponse

@Composable
fun TaskCard(navHostController: NavHostController, priorityColor : Int, statusName : String, taskData: TaskDataResponse){
    Column(modifier = Modifier
        .height(260.dp)
        .width(180.dp)
        .padding(5.dp)
        .clip(RoundedCornerShape(20.dp))
        .background(color = colorResource(priorityColor)),
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Top) {
        Row(
            Modifier
                .padding(10.dp, 10.dp, 10.dp, 0.dp)
                .fillMaxWidth()
                .fillMaxHeight(0.14f),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(painter = painterResource(R.drawable.icon_plug),
                contentDescription = "image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .clip(CircleShape))
            Spacer(Modifier.width(10.dp))
            Text(text= taskData.title?: "Без названия", fontSize = 15.sp, fontWeight = FontWeight.Medium, softWrap = true, maxLines = 2)
        }
        Row(
            Modifier
                .padding(10.dp,10.dp,10.dp,0.dp)
                .fillMaxWidth()
                .fillMaxHeight(0.7f),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top){
            Text(text= taskData.text?: "Без записей", fontSize = 13.sp, fontWeight = FontWeight.Light, softWrap = true, maxLines = 9)
        }
        Column(Modifier.fillMaxSize()) {
            Row(Modifier.padding(10.dp, 10.dp,10.dp,2.dp), horizontalArrangement = Arrangement.Center){
                Text(text= taskData.deadline?: "Без дедлайна", fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
            Row(Modifier.padding(10.dp, 0.dp,10.dp,10.dp), horizontalArrangement = Arrangement.Center){
                Text(text= "Статус: " + statusName, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
fun IsAutorize(){
    val dataStore : UserDataStore = UserDataStore(LocalContext.current)
    val coroutineScope = rememberCoroutineScope()
    var accessToken by remember { mutableStateOf("") }

    coroutineScope.launch {
        accessToken = dataStore.getAccessToken().first()?: ""
    }
    if(!accessToken.equals("")){
        AppNavHost(navController = rememberNavController(), startDestination = Screen.Profile.route)
    }else{
        AppNavHost(navController = rememberNavController(), startDestination = Screen.Login.route)
    }
}

fun RandomString() : String{
    val length = Random.nextInt(2, 4 + 1)
    val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return (1..length)
        .map { charPool[Random.nextInt(0, charPool.size)] }
        .joinToString("")
}

enum class LoadNotesStatus {
    NOT_STARTED,
    COMPLETED
}




