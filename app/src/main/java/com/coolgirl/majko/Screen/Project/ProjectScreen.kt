package com.coolgirl.majko.Screen.Project

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.coolgirl.majko.R
import com.coolgirl.majko.commons.ProjectCard
import com.coolgirl.majko.commons.ProjectCardUiState
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
    val uiStateCard by viewModel.uiStateCard.collectAsState()

    val items = listOf(ModalNavigationDrawerScreens.Task, ModalNavigationDrawerScreens.Project, ModalNavigationDrawerScreens.Group, ModalNavigationDrawerScreens.Profile, ModalNavigationDrawerScreens.Archive)
    val (selectedItem, setSelectedItem) = remember { mutableStateOf(items[1]) }
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
                        SetProjectScreen(uiState, navController, viewModel, uiStateCard)
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

                //панель при длинном нажатии
                if(uiState.is_longtap){
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.1f)
                            .background(color = colorResource(R.color.purple)),
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End){
                        var expanded by remember { mutableStateOf(false) }

                        Box(Modifier.padding(10.dp)) {
                            IconButton(onClick = { expanded = true }) {
                                androidx.compose.material.Icon(
                                    Icons.Default.MoreVert,
                                    contentDescription = "Показать меню"
                                )
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier.fillMaxWidth(0.5f)) {
                                Text(
                                    stringResource(R.string.project_to_archive),
                                    fontSize = 18.sp,
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .clickable {
                                            viewModel.toArchive()
                                        }
                                )
                            }
                        }
                    }
                }
            }

            //экран добавления проекта
            if(uiState.is_adding){
                AddProject(uiState, viewModel)
            }

            if(uiState.is_invite){
                JoinByInviteWindow(uiState, viewModel)
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
fun SetProjectScreen(uiState: ProjectUiState, navController: NavHostController, viewModel: ProjectViewModel, uiStateCard: ProjectCardUiState) {
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
            BasicTextField(
                value = uiState.searchString,
                modifier = Modifier
                    .fillMaxWidth(0.82f)
                    .padding(50.dp, 14.dp, 0.dp, 0.dp),
                textStyle = TextStyle.Default.copy(fontSize = 17.sp, color = colorResource(R.color.white)),
                onValueChange = {viewModel.updateSearchString(it)},
                decorationBox = { innerTextField ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        if (uiState.searchString.isEmpty()) {
                            androidx.compose.material.Text(text = stringResource(R.string.project_search),
                                color = Color.DarkGray,fontSize = 17.sp) }
                        innerTextField() } })

            var expanded by remember { mutableStateOf(false) }

            Box(Modifier.padding(10.dp)) {
                IconButton(onClick = { expanded = true }) {
                    androidx.compose.material.Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "Показать меню",
                        tint = colorResource(R.color.white)
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth(0.5f)) {
                    Text(
                        stringResource(R.string.project_joininvite),
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(10.dp)
                            .clickable {
                                viewModel.openInviteWindow()
                            }
                    )
                }
            }
        }

        val personalProject = uiState.searchPersonalProject
        val groupProject = uiState.searchGroupProject

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
                    ProjectCard(navController, projectData = project, onLongTap = {viewModel.openPanel(it)})
                }
            }

            if (!personalProject.isNullOrEmpty()) {
                item {
                    Text(text = stringResource(R.string.project_personal),
                        color = colorResource(R.color.gray),
                        modifier = Modifier.padding(15.dp, 10.dp, 0.dp, 10.dp))
                }
                items(personalProject) { project ->
                    ProjectCard(navController, projectData = project, onLongTap = {viewModel.openPanel(it)})
                }
            }
        }
    }
}

@Composable
fun JoinByInviteWindow(uiState: ProjectUiState, viewModel: ProjectViewModel){
    Column(
        Modifier
            .fillMaxSize()
            .padding(0.dp, 100.dp, 0.dp, 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top) {
        Column(
            Modifier
                .fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(20.dp))
                .background(colorResource(R.color.purple)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {

            OutlinedTextField(
                value = uiState.invite,
                onValueChange = { viewModel.updateInvite(it) },
                Modifier.padding(20.dp, 20.dp, 20.dp, 20.dp),
                shape = RoundedCornerShape(30.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = colorResource(R.color.white),
                    unfocusedContainerColor = colorResource(R.color.white),
                    focusedBorderColor = colorResource(R.color.white),
                    unfocusedBorderColor = colorResource(R.color.white)
                ),
            )

            if(uiState.invite_message.equals("")){
                Button(onClick = { viewModel.joinByInvite() },
                    shape = CircleShape,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(0.dp, 10.dp),
                    colors = ButtonDefaults.buttonColors(colorResource(R.color.blue))) {
                    Text(text = stringResource(R.string.project_joininvite), color = colorResource(R.color.white),
                        fontSize = 18.sp, fontWeight = FontWeight.Medium)
                }
            }else {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically){
                    Text(text = uiState.invite_message, color = colorResource(R.color.white))
                }

                Button(onClick = { viewModel.openInviteWindow() },
                    shape = CircleShape,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(0.dp, 10.dp),
                    colors = ButtonDefaults.buttonColors(colorResource(R.color.blue))) {
                    Text(text = stringResource(R.string.projectedit_close), color = colorResource(R.color.white),
                        fontSize = 18.sp, fontWeight = FontWeight.Medium)
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