package com.coolgirl.majko.Screen.Task

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.coolgirl.majko.components.TaskCard
import com.coolgirl.majko.R
import com.coolgirl.majko.components.FilterDropdown
import com.coolgirl.majko.components.SearchBox
import com.coolgirl.majko.components.plusButton
import com.coolgirl.majko.navigation.*
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@Composable
fun TaskScreen(navController: NavHostController) {
    val viewModel = getViewModel<TaskViewModel>()

    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit){
        coroutineScope.launch {
            viewModel.loadData()
        }
    }
    val uiState by viewModel.uiState.collectAsState()

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            SetTaskScreen(navController, viewModel, uiState)
        }

        Box(Modifier.align(Alignment.BottomEnd)){
            plusButton(onClick = {navController.navigate(Screen.TaskEditor.createRoute(it))}, id = "0")
        }
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun SetTaskScreen(navController: NavHostController, viewModel: TaskViewModel, uiState: TaskUiState) {
    var expandedFilter by remember { mutableStateOf(false) }

    val allTaskList = uiState.searchAllTaskList
    val favoritesTaskList = uiState.searchFavoritesTaskList

    Column(Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top) {
        Row(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.1f)
                .padding(all = 10.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(color = MaterialTheme.colors.primary),
        verticalAlignment = Alignment.CenterVertically) {

            SearchBox(uiState.searchString, {viewModel.updateSearchString(it)}, R.string.task_search )

            Column {
                Row {
                    Icon(painter = painterResource(R.drawable.icon_filter),
                        modifier = Modifier.clickable {expandedFilter = !expandedFilter },
                        contentDescription = "",
                        tint = MaterialTheme.colors.surface)
                }
                FilterDropdown(expanded = expandedFilter, onExpandedChange = { expandedFilter = it },
                    R.string.filter_task_fav, { viewModel.updateSearchString(uiState.searchString, 1) },
                    R.string.filter_task_each, {viewModel.updateSearchString(uiState.searchString, 0)},
                    R.string.filter_all, {viewModel.updateSearchString(uiState.searchString, 2)})
            }

            Spacer(modifier = Modifier.width(5.dp))

            Icon(painter = painterResource(R.drawable.icon_filter_off),
                modifier = Modifier.clickable { viewModel.updateSearchString(uiState.searchString, 2) },
                contentDescription = "", tint = MaterialTheme.colors.surface)

        }


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
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier.padding(start = 15.dp, top = 10.dp, bottom = 10.dp)
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
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier.padding(start = 15.dp, top = 10.dp, bottom = 10.dp)
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

    }
}