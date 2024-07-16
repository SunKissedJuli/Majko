package com.coolgirl.majko.commons

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
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
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectDataResponse
import com.coolgirl.majko.data.remote.dto.TaskData.TaskDataResponse

@Composable
fun TaskCard(navHostController: NavHostController,
             priorityColor : Int,
             statusName : String,
             taskData: TaskDataResponse,
             onBurnStarClick: (String) -> Unit,
             onDeadStarClick: (String) -> Unit){
    Column(modifier = Modifier
        .height(270.dp)
        .width(180.dp)
        .padding(5.dp)
        .clip(RoundedCornerShape(20.dp))
        .background(color = colorResource(priorityColor))
        .clickable { navHostController.navigate(Screen.TaskEditor.task_id(taskData.id)) },
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Top) {
        Row(
            Modifier
                .padding(10.dp, 10.dp, 10.dp, 0.dp)
                .fillMaxWidth()
                .fillMaxHeight(0.14f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(painter = painterResource(R.drawable.icon_plug),
                contentDescription = "image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .clip(CircleShape))
            Spacer(Modifier.width(5.dp))
            Text(text= taskData.title?: "Без названия", modifier = Modifier.fillMaxWidth(0.7f), fontSize = 14.sp, fontWeight = FontWeight.Medium, softWrap = true, maxLines = 2)
            Spacer(Modifier.width(5.dp))
            if (taskData.is_favorite==true){
                IconButton(onClick = { onBurnStarClick(taskData.id) }) {
                    Icon(imageVector = Icons.Filled.Star, contentDescription = "Favorites", tint = colorResource(R.color.yellow))
                }
            }else{
                IconButton(onClick = { onDeadStarClick(taskData.id) }) {
                    Icon(imageVector = Icons.Outlined.Star, contentDescription = "Favorites", tint = colorResource(R.color.gray))
                }
            }
        }
        Row(
            Modifier
                .padding(10.dp, 10.dp, 10.dp, 0.dp)
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
            if(taskData.project!=null){
                Row(Modifier.padding(10.dp, 0.dp,10.dp,10.dp), horizontalArrangement = Arrangement.Center){
                    Text(text= "Провет: " + taskData.project.name, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
fun ProjectCard(navHostController: NavHostController,
             priorityColor : Int = R.color.white,
             projectData: ProjectDataResponse) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .height(150.dp)
        .padding(10.dp, 10.dp, 10.dp, 0.dp)
        .clip(RoundedCornerShape(20.dp))
        .border(3.dp, color = colorResource(R.color.blue), shape = RoundedCornerShape(20.dp))
        .background(color = colorResource(priorityColor))
        .clickable { navHostController.navigate(Screen.ProjectEditor.project_id(projectData.id)) },
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top) {
        Row(
            Modifier
                .padding(15.dp, 10.dp, 10.dp, 0.dp)
                .fillMaxWidth()
                .fillMaxHeight(0.27f),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically){
            Image(painter = painterResource(R.drawable.icon_plug),
                contentDescription = "image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .clip(CircleShape))
            Spacer(Modifier.width(15.dp))
            Text(text= projectData.name?: "Без названия", modifier = Modifier.fillMaxWidth(0.7f), fontSize = 14.sp, fontWeight = FontWeight.Medium, softWrap = true, maxLines = 2)
        }
        Row(
            Modifier
                .padding(15.dp, 10.dp, 10.dp, 0.dp)
                .fillMaxWidth()
                .fillMaxHeight(0.7f),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top){
            Text(text= projectData.description?: "Без описания", fontSize = 13.sp, fontWeight = FontWeight.Light, softWrap = true, maxLines = 9)
        }
        Column(Modifier.fillMaxSize()) {
            if (!projectData.is_personal){
                for(item in projectData.members){
                    item.name?.let { Text(text = it) }
                }
            }
        }
    }
}

@Composable
fun SpinnerSample(
    name: String,
    items: List<SpinnerItems>,
    selectedItem: String,
    onChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var currentSelectedItem by remember { mutableStateOf(selectedItem) }

    Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
        androidx.compose.material.Text(
            text = name + " " + currentSelectedItem,
            fontSize = 18.sp)
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()) {
            items.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        currentSelectedItem = item.Name
                        onChange(item.Id)})
                {Text(item.Name)}
            }
        }
        Icon(imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = "Expand",
            modifier = Modifier
                .height(24.dp)
                .clickable { expanded = true })
    }
}

data class SpinnerItems(
    val Id:String,
    val Name:  String
)

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

@Composable
fun HorizontalLine(){
    Box(modifier = Modifier
        .fillMaxWidth().padding(0.dp,5.dp,0.dp,12.dp)
        .height(1.dp)
        .background(color = colorResource(R.color.gray))
    )
}

enum class LoadNotesStatus {
    NOT_STARTED,
    COMPLETED
}




