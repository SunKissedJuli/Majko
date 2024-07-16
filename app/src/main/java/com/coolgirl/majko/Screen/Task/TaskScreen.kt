package com.coolgirl.majko.Screen.Task

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.coolgirl.majko.commons.TaskCard
import com.coolgirl.majko.R
import com.coolgirl.majko.data.remote.dto.TaskData.TaskDataResponse
import com.coolgirl.majko.navigation.*
import kotlin.math.roundToInt

@Composable
fun TaskScreen(navController: NavHostController) {
    val viewModel: TaskViewModel = viewModel(
        key = "taskViewModel",
        factory = TaskViewModelProvider.getInstance(LocalContext.current)
    )

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.93f)) {
                SetTaskScreen(navController, viewModel)
            }
            BottomBar(navController, listOf(BottomBarScreens.Notifications, BottomBarScreens.Task, BottomBarScreens.Profile))
        }

        ModalNavigationDrawer(navController,
            listOf(ModalNavigationDrawerScreens.Task, ModalNavigationDrawerScreens.Project,
                ModalNavigationDrawerScreens.Profile, ModalNavigationDrawerScreens.Archive) )

        Button(onClick = { navController.navigate(Screen.TaskEditor.task_id("0"))},
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp, 65.dp)
                .size(56.dp),
            colors = ButtonDefaults.buttonColors(colorResource(R.color.blue))) {
            Text(text = "+", color = colorResource(R.color.white),
                fontSize = 34.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SetTaskScreen(navController: NavHostController, viewModel: TaskViewModel) {
    Column(Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top) {
        Row(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.1f)
                .padding(10.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(color = colorResource(R.color.blue))) {
            // строка поиска, бургер и тд и тп
        }

        val allTaskList = viewModel.uiState.collectAsState().value.allTaskList
        val favoritesTaskList = viewModel.uiState.collectAsState().value.favoritesTaskList
        if (allTaskList != null && favoritesTaskList != null) {
            val columnItems1: Int = ((allTaskList!!.size).toFloat() / 2).roundToInt()
            val columnItems: Int = ((favoritesTaskList!!.size).toFloat() / 2).roundToInt()
            LazyColumn(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (favoritesTaskList != null) {
                    item {
                        Text(text = stringResource(R.string.task_favorites), color = colorResource(R.color.gray),
                            modifier = Modifier.padding(15.dp, 10.dp, 0.dp, 10.dp))
                    }
                    items(columnItems) { columnIndex ->
                        LazyRow(Modifier.fillMaxWidth()) {
                            val count =
                                if (columnIndex == columnItems - 1 && favoritesTaskList.size % 2 != 0) 1 else 2
                            items(count) { rowIndex ->
                                val currentIndex = columnIndex * 2 + rowIndex
                                TaskCard(navController, viewModel.getPriority(favoritesTaskList[currentIndex].priority),
                                    viewModel.getStatus(favoritesTaskList[currentIndex].status), favoritesTaskList[currentIndex],
                                    {viewModel.removeFavotite(it)}, {viewModel.addFavotite(it)})
                            }
                        }
                    }
                }

                if (allTaskList != null) {
                    item {
                        Text(text = stringResource(R.string.task_each), color = colorResource(R.color.gray),
                            modifier = Modifier.padding(15.dp, 10.dp, 0.dp, 10.dp))
                    }

                    items(columnItems1) { columnIndex ->
                        LazyRow(Modifier.fillMaxWidth()) {
                            val count = if (columnIndex == columnItems1 - 1 && allTaskList.size % 2 != 0) 1 else 2
                            items(count) { rowIndex ->
                                val currentIndex = columnIndex * 2 + rowIndex
                                TaskCard(navController, viewModel.getPriority(allTaskList[currentIndex].priority),
                                    viewModel.getStatus(allTaskList[currentIndex].status), allTaskList[currentIndex],
                                    {viewModel.removeFavotite(it)}, {viewModel.addFavotite(it)})
                            }
                        }
                    }
                }
            }
        }
    }
}