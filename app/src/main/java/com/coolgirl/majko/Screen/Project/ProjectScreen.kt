package com.coolgirl.majko.Screen.Project

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.coolgirl.majko.R
import com.coolgirl.majko.commons.ProjectCard
import com.coolgirl.majko.navigation.BottomBar
import com.coolgirl.majko.navigation.BottomBarScreens
import com.coolgirl.majko.navigation.ModalNavigationDrawerScreens
import kotlinx.coroutines.launch

@Composable
fun ProjectScreen(navController: NavHostController){
    val viewModel: ProjectViewModel = viewModel(
        key = "taskViewModel",
        factory = ProjectViewModelProvider.getInstance(LocalContext.current)
    )
    val uiState by viewModel.uiState.collectAsState()

    val items = listOf(ModalNavigationDrawerScreens.Task, ModalNavigationDrawerScreens.Project, ModalNavigationDrawerScreens.Profile, ModalNavigationDrawerScreens.Archive)
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
                        label = { Text(item.title, fontSize = 22.sp) },
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
            Box(
                Modifier
                    .fillMaxSize()
                    .alpha(uiState.is_adding_background)) {
                Column(Modifier.fillMaxSize()) {

                    Column(
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.93f)) {
                        SetProjectScreen(uiState, navController)
                    }
                    BottomBar(navController, listOf(BottomBarScreens.Notifications, BottomBarScreens.Task, BottomBarScreens.Profile))
                }


                Button(onClick = { viewModel.addingProject()},
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

            //экран добавления проекта
            if(uiState.is_adding){
                AddProject(uiState, viewModel)
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
fun SetProjectScreen(uiState: ProjectUiState, navController: NavHostController) {
    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.1f)
                .padding(10.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(color = colorResource(R.color.blue))
        ) {
            // строка поиска, бургер и тд и тп
        }

        val personalProject = uiState.personalProject
        val groupProject = uiState.groupProject

        LazyColumn(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (!groupProject.isNullOrEmpty()) {
                item {
                    Text(text = stringResource(R.string.project_group),
                        color = colorResource(R.color.gray),
                        modifier = Modifier.padding(15.dp, 10.dp, 0.dp, 10.dp))
                }
                items(groupProject) { project ->
                    ProjectCard(navController, projectData = project)
                }
            }

            if (!personalProject.isNullOrEmpty()) {
                item {
                    Text(text = stringResource(R.string.project_personal),
                        color = colorResource(R.color.gray),
                        modifier = Modifier.padding(15.dp, 10.dp, 0.dp, 10.dp))
                }
                items(personalProject) { project ->
                    ProjectCard(navController, projectData = project)
                }
            }
        }
    }
}

@Composable
fun AddProject(uiState: ProjectUiState, viewModel: ProjectViewModel){
    Column(Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Column(
            Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.5f)
                .clip(RoundedCornerShape(25.dp))
                .background(colorResource(R.color.blue)),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start) {

            OutlinedTextField(
                value = uiState.newProjectName,
                onValueChange = {viewModel.updateProjectName(it)},
                Modifier.padding(20.dp, 20.dp, 0.dp, 0.dp),
                shape = RoundedCornerShape(30.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = colorResource(R.color.white), unfocusedContainerColor = colorResource(R.color.white),
                    focusedBorderColor = colorResource(R.color.white), unfocusedBorderColor = colorResource(R.color.white)),
                placeholder = {Text(text = stringResource(R.string.project_name), color = colorResource(R.color.gray))})

            OutlinedTextField(value = uiState.newProjectDescription,
                onValueChange = {viewModel.updateProjectDescription(it)},
                Modifier
                    .fillMaxHeight(0.75f)
                    .padding(20.dp, 20.dp, 0.dp, 20.dp),
                shape = RoundedCornerShape(30.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = colorResource(R.color.white), unfocusedContainerColor = colorResource(R.color.white),
                    focusedBorderColor = colorResource(R.color.white), unfocusedBorderColor = colorResource(R.color.white)),
                placeholder = {Text(text = stringResource(R.string.project_description), color = colorResource(R.color.gray))})

            Row(Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center){
                Button(onClick = { viewModel.addProject()},
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .border(3.dp, colorResource(R.color.white), RoundedCornerShape(30.dp)),
                    colors = ButtonDefaults.buttonColors(colorResource(R.color.blue))) {
                    Text(text = stringResource(R.string.project_add), color = colorResource(R.color.white),
                        fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }


        }
    }
}