package com.coolgirl.majko.Screen.Task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.coolgirl.majko.commons.TaskCard
import com.coolgirl.majko.R
import com.coolgirl.majko.navigation.*
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun TaskScreen(navController: NavHostController) {
    val viewModel: TaskViewModel = viewModel(
        key = "taskViewModel",
        factory = TaskViewModelProvider.getInstance(LocalContext.current)
    )

    val uiState by viewModel.uiState.collectAsState()

    val items = listOf(ModalNavigationDrawerScreens.Task, ModalNavigationDrawerScreens.Project, ModalNavigationDrawerScreens.Group, ModalNavigationDrawerScreens.Profile, ModalNavigationDrawerScreens.Archive)
    val (selectedItem, setSelectedItem) = remember { mutableStateOf(items[0]) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                Modifier.fillMaxWidth(0.7f),
                drawerContainerColor = colorResource(R.color.purple)) {
                items.forEach { item ->
                    NavigationDrawerItem(
                        label = { androidx.compose.material3.Text(item.title, fontSize = 22.sp) },
                        selected = selectedItem == item,
                        onClick = {
                            setSelectedItem(item)
                            navController.navigate(item.route)
                            scope.launch { drawerState.close() } },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = Color.Transparent, unselectedContainerColor = Color.Transparent,
                            selectedTextColor = Color.Black, unselectedTextColor = Color.White)
                    )
                }
            }
        },
        content = {
            Box(Modifier.fillMaxSize()) {
                Column(Modifier.fillMaxSize()) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.93f)) {
                        SetTaskScreen(navController, viewModel, uiState)
                    }
                    BottomBar(navController, listOf(BottomBarScreens.Notifications, BottomBarScreens.Task, BottomBarScreens.Profile))
                }

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

            Row(Modifier.padding(10.dp)) {
                IconButton(
                    onClick = { scope.launch { drawerState.open() } },
                    content = {
                        Icon(
                            Icons.Filled.Menu,
                            contentDescription = "Menu",
                            tint = colorResource(R.color.white))
                    }
                )
            }
        }
    )
}

@Composable
fun SetTaskScreen(navController: NavHostController, viewModel: TaskViewModel, uiState: TaskUiState) {
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

            BasicTextField(
                value = uiState.searchString,
                modifier = Modifier.padding(50.dp, 14.dp, 0.dp,0.dp),
                textStyle = TextStyle.Default.copy(fontSize = 17.sp, color = colorResource(R.color.white)),
                onValueChange = {viewModel.updateSearchString(it)},
                decorationBox = { innerTextField ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        if (uiState.searchString.isEmpty()) {
                            Text(text = stringResource(R.string.task_search),
                                color = Color.DarkGray,fontSize = 17.sp) }
                        innerTextField() } })
            // строка поиска, бургер и тд и тп
        }

        val allTaskList = viewModel.uiState.collectAsState().value.searchAllTaskList
        val favoritesTaskList = viewModel.uiState.collectAsState().value.searchFavoritesTaskList
       // if (allTaskList != null && favoritesTaskList != null) {
            LazyColumn(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)) {

                if (favoritesTaskList != null && favoritesTaskList.isNotEmpty()) {
                    item {
                        Text(
                            text = stringResource(R.string.task_favorites),
                            color = colorResource(R.color.gray),
                            modifier = Modifier.padding(15.dp, 10.dp, 0.dp, 10.dp)
                        )
                    }
                    items(favoritesTaskList.size / 2 + favoritesTaskList.size % 2) { rowIndex ->
                        Row(Modifier.fillMaxWidth()) {
                            val firstIndex = rowIndex * 2
                            if (firstIndex < favoritesTaskList.size) {
                                Column(Modifier.fillMaxWidth(0.5f)) {
                                    TaskCard(navController, viewModel.getPriority(favoritesTaskList[firstIndex].priority),
                                        viewModel.getStatus(favoritesTaskList[firstIndex].status), taskData = favoritesTaskList[firstIndex],
                                        { viewModel.removeFavotite(it) }, { viewModel.addFavotite(it) }
                                    )
                                }

                            }

                            val secondIndex = firstIndex + 1
                            if (secondIndex < favoritesTaskList.size) {
                                Column(Modifier.fillMaxWidth()) {
                                    TaskCard(navController, viewModel.getPriority(favoritesTaskList[secondIndex].priority),
                                        viewModel.getStatus(favoritesTaskList[secondIndex].status), taskData = favoritesTaskList[secondIndex],
                                        { viewModel.removeFavotite(it) }, { viewModel.addFavotite(it) }
                                    )
                                }

                            }
                        }
                    }
                }

                if (allTaskList != null && allTaskList.isNotEmpty()) {
                    item {
                        Text(
                            text = stringResource(R.string.task_each),
                            color = colorResource(R.color.gray),
                            modifier = Modifier.padding(15.dp, 10.dp, 0.dp, 10.dp)
                        )
                    }
                    items(allTaskList.size / 2 + allTaskList.size % 2) { rowIndex ->
                        Row(Modifier.fillMaxWidth()) {
                            val firstIndex = rowIndex * 2
                            if (firstIndex < allTaskList.size) {
                                Column(Modifier.fillMaxWidth(0.5f)) {
                                    TaskCard(navController, viewModel.getPriority(allTaskList[firstIndex].priority),
                                        viewModel.getStatus(allTaskList[firstIndex].status), taskData = allTaskList[firstIndex],
                                        { viewModel.removeFavotite(it) }, { viewModel.addFavotite(it) }
                                    )
                                }

                            }

                            val secondIndex = firstIndex + 1
                            if (secondIndex < allTaskList.size) {
                                Column(Modifier.fillMaxWidth()) {
                                    TaskCard(navController, viewModel.getPriority(allTaskList[secondIndex].priority),
                                        viewModel.getStatus(allTaskList[secondIndex].status), taskData = allTaskList[secondIndex],
                                        { viewModel.removeFavotite(it) }, { viewModel.addFavotite(it) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
      //  }
    }
}