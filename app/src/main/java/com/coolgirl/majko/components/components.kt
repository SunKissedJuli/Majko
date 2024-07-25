package com.coolgirl.majko.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.coolgirl.majko.R
import com.coolgirl.majko.data.remote.dto.GroupData.GroupResponse
import com.coolgirl.majko.data.remote.dto.ProjectData.ProjectDataResponse
import com.coolgirl.majko.data.remote.dto.TaskData.TaskDataResponse
import com.coolgirl.majko.navigation.Screen
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@Composable
fun plusButton(onClick: (String) -> Unit, id: String){

    Button(onClick = { onClick(id)},
        shape = CircleShape,
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 65.dp)
            .size(56.dp),
        colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)) {
        Image(painter = painterResource(R.drawable.icon_plus), contentDescription = "")
    }
}

@Composable
fun HorizontalLine(){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 5.dp, bottom = 12.dp)
        .height(1.dp)
        .background(color = MaterialTheme.colors.surface)
    )
}

@Composable
fun TaskCard(navHostController: NavHostController,
             priorityColor : Int,
             statusName : String,
             taskData: TaskDataResponse,
             onBurnStarClick: (String) -> Unit,
             onDeadStarClick: (String) -> Unit){
    Column(modifier = Modifier
        .height(280.dp)
        .fillMaxWidth()
        .padding(5.dp)
        .clip(RoundedCornerShape(20.dp))
        .background(color = colorResource(priorityColor))
        .clickable { navHostController.navigate(Screen.TaskEditor.createRoute(taskData.id)) },
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top) {
        Row(
            Modifier
                .padding(start = 10.dp, top = 10.dp, end = 10.dp)
                .fillMaxWidth()
                .fillMaxHeight(0.14f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(painter = painterResource(R.drawable.icon_plug),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .clip(CircleShape))
            Spacer(Modifier.width(5.dp))
            Text(text= taskData.title?: stringResource(R.string.common_noname), modifier = Modifier.fillMaxWidth(0.7f), fontSize = 14.sp, fontWeight = FontWeight.Medium, softWrap = true, maxLines = 2)
            Spacer(Modifier.width(5.dp))
            if (taskData.is_favorite==true){
                IconButton(onClick = { onBurnStarClick(taskData.id) }) {
                    Icon(imageVector = Icons.Filled.Star, contentDescription = "", tint = colorResource(R.color.yellow))
                }
            }else{
                IconButton(onClick = { onDeadStarClick(taskData.id) }) {
                    Icon(imageVector = Icons.Outlined.Star, contentDescription = "", tint = colorResource(R.color.gray))
                }
            }
        }
        Row(
            Modifier
                .padding(start = 10.dp, top = 10.dp, end = 10.dp)
                .fillMaxWidth()
                .fillMaxHeight(0.64f),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top){
            Text(text= taskData.text?: stringResource(R.string.common_nonote), fontSize = 13.sp, fontWeight = FontWeight.Light, softWrap = true, maxLines = 9)
        }
        Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.Bottom){
            Column(
                Modifier
                    .fillMaxWidth(0.8f)
                    .padding(bottom = 8.dp), verticalArrangement = Arrangement.Bottom) {
                Row(Modifier.padding(start = 10.dp, top = 5.dp, bottom = 2.dp), horizontalArrangement = Arrangement.Center){

                    val dateTime = LocalDateTime.parse(taskData.deadline, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    Text(text= dateTime.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("ru"))
                            + ", " + dateTime.dayOfMonth + " "
                            + dateTime.month.getDisplayName(TextStyle.FULL, Locale("ru")), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
                Row(Modifier.padding(start = 10.dp, bottom = 2.dp), horizontalArrangement = Arrangement.Center){
                    Text(text= stringResource(R.string.taskeditor_status) + " " + statusName, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
                if(taskData.project!=null){
                    Row(Modifier.padding(start = 10.dp), horizontalArrangement = Arrangement.Center){
                        Text(text= stringResource(R.string.projectedit_project) + " " + taskData.project.name, fontSize = 13.sp, fontWeight = FontWeight.Medium, maxLines = 2)
                    }
                }
            }

            Row(
                Modifier
                    .fillMaxSize()
                    .padding(bottom = 10.dp),
                verticalAlignment = Alignment.Bottom){
                Image(painter = painterResource(R.drawable.icon_subtask),
                    contentDescription = "",
                    Modifier.size(20.dp))
                Text(text = taskData.count_subtasks.toString())
            }
        }
    }
}

data class ProjectCardUiState(
    var borderColor: Int = R.color.gray,
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProjectCard(navHostController: NavHostController,
                priorityColor : Int = R.color.white,
                projectData: ProjectDataResponse,
                is_archive:Boolean=false,
                onLongTap:(String) -> Unit) {

    var tapType by remember { mutableStateOf(R.color.gray) }
    Column(modifier = Modifier
        .fillMaxWidth()
        .height(170.dp)
        .padding(start = 10.dp, top = 10.dp, end = 10.dp)
        .clip(RoundedCornerShape(20.dp))
        .border(
            3.dp,
            color = colorResource(tapType),
            shape = RoundedCornerShape(20.dp)
        )
        .background(color = colorResource(priorityColor))
        .combinedClickable(
            onClick = { navHostController.navigate(Screen.ProjectEditor.createRoute(projectData.id)) },
            onLongClick = {
                tapType = R.color.purple
                onLongTap(projectData.id)
            },
        ),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top) {
        Row(
            Modifier
                .padding(start = 15.dp, top = 10.dp, end = 10.dp)
                .fillMaxWidth()
                .fillMaxHeight(0.27f),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically){
            Image(painter = painterResource(R.drawable.icon_plug),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .clip(CircleShape))
            Spacer(Modifier.width(15.dp))
            Text(text= projectData.name?: stringResource(R.string.common_noname), modifier = Modifier.fillMaxWidth(0.7f), fontSize = 14.sp, fontWeight = FontWeight.Medium, softWrap = true, maxLines = 2)

        }
        Row(
            Modifier
                .padding(start = 15.dp, top = 10.dp, end = 10.dp)
                .fillMaxWidth()
                .fillMaxHeight(0.7f),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top){
            Text(text= projectData.description?: stringResource(R.string.common_nodescription), fontSize = 13.sp, fontWeight = FontWeight.Light, softWrap = true, maxLines = 4)
        }
        Row(
            Modifier
                .fillMaxSize()
                .padding(end = 15.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End) {
            if (!projectData.is_personal){
                Icon(imageVector = Icons.Filled.Person, contentDescription = "", tint = colorResource(R.color.black))
                Text(text = projectData.members.size.toString())
            }
        }
    }
}

@Composable
fun GroupCard(navHostController: NavHostController,
              priorityColor : Int = R.color.white,
              groupData: GroupResponse, ) {

    var tapType by remember { mutableStateOf(R.color.gray) }
    Column(modifier = Modifier
        .fillMaxWidth()
        .height(150.dp)
        .padding(start = 10.dp, top = 10.dp, end = 10.dp)
        .clip(RoundedCornerShape(20.dp))
        .border(3.dp, color = colorResource(tapType), shape = RoundedCornerShape(20.dp))
        .background(color = colorResource(priorityColor))
        .clickable { navHostController.navigate(Screen.GroupEditor.createRoute(groupData.id)) },
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top) {
        Row(
            Modifier
                .padding(start = 15.dp, top = 10.dp, end = 10.dp)
                .fillMaxWidth()
                .fillMaxHeight(0.27f),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically){
            Image(painter = painterResource(R.drawable.icon_plug),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .clip(CircleShape))
            Spacer(Modifier.width(15.dp))
            Text(text= groupData.title?: stringResource(R.string.common_noname), modifier = Modifier.fillMaxWidth(0.7f), fontSize = 14.sp, fontWeight = FontWeight.Medium, softWrap = true, maxLines = 2)

        }
        Row(
            Modifier
                .padding(start = 15.dp, top = 10.dp, end = 10.dp)
                .fillMaxWidth()
                .fillMaxHeight(0.7f),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top){
            Text(text= groupData.description?: stringResource(R.string.common_nodescription), fontSize = 13.sp, fontWeight = FontWeight.Light, softWrap = true, maxLines = 9)
        }
        Row(
            Modifier
                .fillMaxSize()
                .padding(end = 15.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End) {
            if (!groupData.is_personal){
                Icon(imageVector = Icons.Filled.Person, contentDescription = "", tint = colorResource(R.color.black))
                Text(text = groupData.members.size.toString())
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

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {
        Text(text = name + " " + currentSelectedItem, fontSize = 18.sp)
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()) {
            items.forEach { item ->
                DropdownMenuItem(
                    modifier = Modifier.background(MaterialTheme.colors.secondary),
                    onClick = {
                        expanded = false
                        currentSelectedItem = item.Name
                        onChange(item.Id)})
                { Text(item.Name) }
            }
        }
        Icon(imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = "",
            modifier = Modifier
                .height(24.dp)
                .clickable { expanded = true })
    }
}

data class SpinnerItems(
    val Id:String,
    val Name:  String
)