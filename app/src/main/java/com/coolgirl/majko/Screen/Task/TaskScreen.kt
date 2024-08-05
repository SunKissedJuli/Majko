package com.coolgirl.majko.Screen.Task

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.coolgirl.majko.R
import com.coolgirl.majko.components.*
import com.coolgirl.majko.navigation.*
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun TaskScreen(navController: NavHostController) {
    val viewModel: TaskViewModel = koinViewModel()

    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit){
        coroutineScope.launch {
            viewModel.loadData()
        }
    }
    val uiState by viewModel.uiState.collectAsState()

    //снекбары
    Box(Modifier.fillMaxSize()) {
        if(uiState.isError){
            Row(Modifier.align(Alignment.BottomCenter)) {
                uiState.errorMessage?.let { ErrorSnackbar(it, { viewModel.isError(null) }) }
            }
        }
        if(uiState.isMessage){
            Row(Modifier.align(Alignment.BottomCenter)) {
                uiState.message?.let { MessageSnackbar(it, { viewModel.isMessage(null) }) }
            }
        }
    }

    Scaffold (
        topBar = {

            //панель при длинном нажатии
            if (uiState.isLongtap) {
                LongTapPanel(viewModel::updateStatus, viewModel::removeTask, uiState, viewModel::updateExpandedLongTap )
            } else {

                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(65.dp)
                        .padding(all = 10.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .background(color = MaterialTheme.colorScheme.primary),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    SearchBox(uiState.searchString, { viewModel.updateSearchString(it, 2) }, R.string.task_search)

                    Column {
                        Row {
                            IconButton(onClick = { viewModel.updateExpandedFilter() }, Modifier.size(27.dp)) {
                                Icon(painter = painterResource(R.drawable.icon_filter),
                                    contentDescription = "", tint = MaterialTheme.colorScheme.background)
                            }

                        }
                        FilterDropdown(expanded = uiState.expandedFilter, onDismissRequest = {  viewModel.updateExpandedFilter() },
                            R.string.filter_task_fav, { viewModel.updateSearchString(uiState.searchString, it) },
                            R.string.filter_task_each, R.string.filter_all)
                    }

                    Spacer(modifier = Modifier.width(5.dp))

                    IconButton(onClick = { viewModel.updateSearchString(uiState.searchString, 2) }, Modifier.size(27.dp)) {
                        Icon(painter = painterResource(R.drawable.icon_filter_off),
                            contentDescription = "", tint = MaterialTheme.colorScheme.background)
                    }
                }
            }
        }
    ){
        Box(
            Modifier
                .fillMaxSize()
                .padding(it)
                .background(MaterialTheme.colorScheme.background)) {
            Column(Modifier.fillMaxSize()) {
                SetTaskScreen(navController, viewModel, uiState)
            }

            Box(Modifier.align(Alignment.BottomEnd)){
                AddButton(onClick = {navController.navigate(Screen.TaskEditor.createRoute(it))}, id = "0")
            }
        }
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun SetTaskScreen(navController: NavHostController, viewModel: TaskViewModel, uiState: TaskUiState) {
    val allTaskList = uiState.searchAllTaskList
    val favoritesTaskList = uiState.searchFavoritesTaskList

    Column(Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top) {

        LazyColumn(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)) {

            if (!favoritesTaskList.isNullOrEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.task_favorites),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(start = 15.dp, top = 10.dp, bottom = 10.dp)
                    )
                }
                items(favoritesTaskList.size / 2 + favoritesTaskList.size % 2) { rowIndex ->
                    Row(Modifier.fillMaxWidth()) {
                        val firstIndex = rowIndex * 2
                        if (firstIndex < favoritesTaskList.size) {
                            Column(Modifier.fillMaxWidth(0.5f)) {
                                TaskCard(navController,  colorResource(viewModel.getPriority(favoritesTaskList[firstIndex].priority)),
                                    viewModel.getStatus(favoritesTaskList[firstIndex].status), taskData = favoritesTaskList[firstIndex],
                                    { viewModel.removeFavotite(it) }, { viewModel.addFavotite(it) },
                                    onLongTap = { viewModel.openPanel(it) },
                                    onLongTapRelease = { viewModel.openPanel(it) },
                                    isSelected = uiState.longtapTaskId.contains(favoritesTaskList[firstIndex].id)
                                )
                            }

                        }

                        val secondIndex = firstIndex + 1
                        if (secondIndex < favoritesTaskList.size) {
                            Column(Modifier.fillMaxWidth()) {
                                TaskCard(navController,  colorResource(viewModel.getPriority(favoritesTaskList[secondIndex].priority)),
                                    viewModel.getStatus(favoritesTaskList[secondIndex].status), taskData = favoritesTaskList[secondIndex],
                                    { viewModel.removeFavotite(it) }, { viewModel.addFavotite(it) },
                                    onLongTap = { viewModel.openPanel(it) },
                                    onLongTapRelease = { viewModel.openPanel(it) },
                                    isSelected = uiState.longtapTaskId.contains(favoritesTaskList[secondIndex].id)
                                )
                            }

                        }
                    }
                }
            }

            if (!allTaskList.isNullOrEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.task_each),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(start = 15.dp, top = 10.dp, bottom = 10.dp)
                    )
                }
                items(allTaskList.size / 2 + allTaskList.size % 2) { rowIndex ->
                    Row(Modifier.fillMaxWidth()) {
                        val firstIndex = rowIndex * 2
                        if (firstIndex < allTaskList.size) {
                            Column(Modifier.fillMaxWidth(0.5f)) {
                                TaskCard(navController,  colorResource(viewModel.getPriority(allTaskList[firstIndex].priority)),
                                    viewModel.getStatus(allTaskList[firstIndex].status), taskData = allTaskList[firstIndex],
                                    { viewModel.removeFavotite(it) }, { viewModel.addFavotite(it) },
                                onLongTap = { viewModel.openPanel(it) },
                                onLongTapRelease = { viewModel.openPanel(it) },
                                isSelected = uiState.longtapTaskId.contains(allTaskList[firstIndex].id)
                                )
                            }

                        }

                        val secondIndex = firstIndex + 1
                        if (secondIndex < allTaskList.size) {
                            Column(Modifier.fillMaxWidth()) {
                                TaskCard(navController,  colorResource(viewModel.getPriority(allTaskList[secondIndex].priority)),
                                    viewModel.getStatus(allTaskList[secondIndex].status), taskData = allTaskList[secondIndex],
                                    { viewModel.removeFavotite(it) }, { viewModel.addFavotite(it) },
                                    onLongTap = { viewModel.openPanel(it) },
                                    onLongTapRelease = { viewModel.openPanel(it) },
                                    isSelected = uiState.longtapTaskId.contains(allTaskList[secondIndex].id)
                                )
                            }
                        }
                    }
                }
            }
        }

    }
}

@Composable
private fun LongTapPanel(onUpdateStatus: ()-> Unit, onRemoveTask: ()-> Unit,
                         uiState: TaskUiState, onExpandedLongTap: ()->Unit){
    Row(
        Modifier
            .fillMaxWidth()
            .height(65.dp)
            .background(color = MaterialTheme.colorScheme.secondary),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End){


        Box(Modifier.padding(all = 10.dp)) {
            IconButton(onClick = {onExpandedLongTap() }) {
                Icon(painter = painterResource(R.drawable.icon_menu),
                    contentDescription = "", tint = MaterialTheme.colorScheme.background)
            }
            DropdownMenu(
                expanded = uiState.expandedLongTap,
                onDismissRequest = { onExpandedLongTap() },
                modifier = Modifier.fillMaxWidth(0.5f)) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            onUpdateStatus()
                            onExpandedLongTap()
                        }) {
                    androidx.compose.material3.Text(
                        stringResource(R.string.task_updatestatus),
                        fontSize = 18.sp,
                        modifier = Modifier.padding(all = 10.dp)
                    )
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            onRemoveTask()
                            onExpandedLongTap()
                        }) {
                    androidx.compose.material3.Text(
                        stringResource(R.string.project_delite),
                        fontSize = 18.sp,
                        modifier = Modifier.padding(all = 10.dp)
                    )
                }
            }
        }
    }
}