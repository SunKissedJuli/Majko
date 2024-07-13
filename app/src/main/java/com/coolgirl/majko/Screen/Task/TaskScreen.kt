package com.coolgirl.majko.Screen.Task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.coolgirl.majko.commons.TaskCard
import com.coolgirl.majko.data.dataStore.UserDataStore
import com.coolgirl.majko.navigation.BottomBar
import com.coolgirl.majko.navigation.BottomBarScreens
import kotlinx.coroutines.launch
import com.coolgirl.majko.R
import com.coolgirl.majko.Screen.Profile.ProfileViewModel
import kotlin.math.roundToInt

@Composable
fun TaskScreen(navController: NavHostController){
    val viewModel: TaskViewModel = viewModel(key = "taskViewModel",
    factory =  TaskViewModelProvider.getInstance(LocalContext.current))

    Column(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.94f)) {
            SetTaskScreen(navController, viewModel)
        }
        BottomBar(navController, listOf(BottomBarScreens.Notifications, BottomBarScreens.Task, BottomBarScreens.Profile))
    }

}

@Composable
fun SetTaskScreen(navController: NavHostController, viewModel: TaskViewModel) {
    Column(Modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top) {
        Row(Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.1f)
                .padding(10.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(color = colorResource(R.color.blue))) {
            //строка поиска, бургер и тд и тп
        }

        var allTaskList = viewModel.uiState.collectAsState().value.allTaskList
        if(allTaskList!=null){
            val columnItems: Int = ((allTaskList!!.size)!!.toFloat() / 2).roundToInt()
            LazyColumn(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally) {
                items(columnItems) { columnIndex ->
                    LazyRow(Modifier.fillMaxWidth()) {
                        val count =
                            if (columnIndex == columnItems - 1 && allTaskList!!.size % 2 != 0) 1 else 2
                        items(count) { rowIndex ->
                            val currentIndex = columnIndex * 2 + rowIndex
                            TaskCard(navController, viewModel.getPriority(allTaskList!![currentIndex].priority),
                                viewModel.getStatus(allTaskList!![currentIndex].status), taskData = allTaskList!![currentIndex])
                        }
                    }
                }
            }
        }
    }
}