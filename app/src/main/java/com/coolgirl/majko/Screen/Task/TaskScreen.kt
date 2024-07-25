package com.coolgirl.majko.Screen.Task

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.coolgirl.majko.components.TaskCard
import com.coolgirl.majko.R
import com.coolgirl.majko.Screen.Profile.ProfileViewModel
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
            Column(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.93f)) {
                SetTaskScreen(navController, viewModel, uiState)
            }
        }

        Box(Modifier.align(Alignment.BottomEnd)){
            plusButton(onClick = {navController.navigate(Screen.TaskEditor.createRoute(it))}, id = "0")
        }
    }
}

@SuppressLint("SuspiciousIndentation")
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
                .padding(all = 10.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(color = MaterialTheme.colors.primary),
        verticalAlignment = Alignment.CenterVertically) {

            BasicTextField(
                value = uiState.searchString,
                modifier = Modifier.padding(start = 50.dp),
                textStyle = TextStyle.Default.copy(fontSize = 17.sp, color = MaterialTheme.colors.background),
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
                            color = colorResource(R.color.gray),
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
      //  }
    }
}