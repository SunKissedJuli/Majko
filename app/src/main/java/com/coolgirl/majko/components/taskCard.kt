package com.coolgirl.majko.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.coolgirl.majko.R
import com.coolgirl.majko.data.remote.dto.TaskData.TaskDataResponse
import com.coolgirl.majko.navigation.Screen
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskCard(navHostController: NavHostController,
             priorityColor : Color,
             statusName : String,
             taskData: TaskDataResponse,
             onBurnStarClick: (String) -> Unit = {},
             onDeadStarClick: (String) -> Unit = {},
             onLongTap: (String) -> Unit = {},
             onLongTapRelease: (String) -> Unit = {},
             isSelected: Boolean = false){

    val borderColor = if (isSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.background

    Column(modifier = Modifier
        .height(280.dp)
        .fillMaxWidth()
        .padding(5.dp)
        .clip(RoundedCornerShape(20.dp))
        .background(color = priorityColor)
        .border(3.dp, color = borderColor, shape = RoundedCornerShape(20.dp))
        .combinedClickable(
            onClick = { navHostController.navigate(Screen.TaskEditor.createRoute(taskData.id)) },
            onLongClick = {
                if (isSelected) {
                    onLongTapRelease(taskData.id)
                } else {
                    onLongTap(taskData.id)
                }
            },
        ),
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
            Box(
                Modifier
                    .fillMaxHeight(0.8f)
                    .size(27.dp)
                    .aspectRatio(1f)
                    .background(MaterialTheme.colorScheme.primary, shape = CircleShape))

            Spacer(Modifier.width(7.dp))
            Text(text= taskData.title?: stringResource(R.string.common_noname), modifier = Modifier.fillMaxWidth(0.7f), fontSize = 14.sp, fontWeight = FontWeight.Medium, softWrap = true, maxLines = 2)
            Spacer(Modifier.width(5.dp))
            if (taskData.isFavorite==true){
                IconButton(onClick = { onBurnStarClick(taskData.id) }) {
                    Icon(imageVector = Icons.Filled.Star, contentDescription = "", tint = colorResource(
                        R.color.yellow_star)
                    )
                }
            }else{
                IconButton(onClick = { onDeadStarClick(taskData.id) }) {
                    Icon(imageVector = Icons.Outlined.Star, contentDescription = "", tint = colorResource(
                        R.color.gray)
                    )
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
                Text(text = taskData.countSubtasks.toString())
            }
        }
    }
}
