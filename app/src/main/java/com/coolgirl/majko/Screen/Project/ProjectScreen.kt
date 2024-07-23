package com.coolgirl.majko.Screen.Project

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.coolgirl.majko.R
import com.coolgirl.majko.components.ProjectCard
import com.coolgirl.majko.components.ProjectCardUiState
import com.coolgirl.majko.components.plusButton
import com.coolgirl.majko.navigation.BottomBar
import com.coolgirl.majko.navigation.BottomBarScreens

@Composable
fun ProjectScreen(navController: NavHostController){
    val viewModel: ProjectViewModel = viewModel(
        key = "taskViewModel",
        factory = ProjectViewModelProvider.getInstance(LocalContext.current)
    )
    val uiState by viewModel.uiState.collectAsState()
    val uiStateCard by viewModel.uiStateCard.collectAsState()

    Box(
        Modifier
            .fillMaxSize()
            .alpha(uiState.isAddingBackground)) {
        Column(Modifier.fillMaxSize()) {

            Column(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.93f)) {
                SetProjectScreen(uiState, navController, viewModel, uiStateCard)
            }
        }

        Box(Modifier.align(Alignment.BottomEnd)){
            plusButton(onClick = {viewModel.addingProject()}, id = "")
        }

        //панель при длинном нажатии
        if(uiState.isLongtap){
            Row(Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.1f)
                    .background(color = MaterialTheme.colors.secondary),
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End){
                var expanded by remember { mutableStateOf(false) }

                Box(Modifier.padding(all = 10.dp)) {
                    IconButton(onClick = { expanded = true }) {
                        Image(painter = painterResource(R.drawable.icon_menu),
                            contentDescription = "")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth(0.5f)) {
                        Text(
                            stringResource(R.string.project_to_archive),
                            fontSize = 18.sp,
                            modifier = Modifier
                                .padding(all = 10.dp)
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
    if(uiState.isAdding){
        AddProject(uiState, viewModel)
    }

    if(uiState.isInvite){
        JoinByInviteWindow(uiState, viewModel)
    }
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
                .padding(all = 10.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(color = MaterialTheme.colors.primary),
            verticalAlignment = Alignment.CenterVertically) {
            BasicTextField(
                value = uiState.searchString,
                modifier = Modifier
                    .fillMaxWidth(0.82f)
                    .padding(start = 50.dp),
                textStyle = TextStyle.Default.copy(fontSize = 17.sp, color = MaterialTheme.colors.background),
                onValueChange = {viewModel.updateSearchString(it)},
                decorationBox = { innerTextField ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        if (uiState.searchString.isEmpty()) {
                           Text(text = stringResource(R.string.project_search),
                                color = Color.DarkGray,fontSize = 17.sp) }
                        innerTextField() } })

            var expanded by remember { mutableStateOf(false) }

            Box(Modifier.padding(10.dp)) {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "",
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
                            .padding(all = 10.dp)
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
                        modifier = Modifier.padding(start = 15.dp, top = 10.dp, bottom = 10.dp))
                }
                items(groupProject) { project ->
                    ProjectCard(navController, projectData = project, onLongTap = {viewModel.openPanel(it)})
                }
            }

            if (!personalProject.isNullOrEmpty()) {
                item {
                    Text(text = stringResource(R.string.project_personal),
                        color = MaterialTheme.colors.surface,
                        modifier = Modifier.padding(start = 15.dp, top = 10.dp, bottom = 10.dp))
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
            .padding(top = 100.dp),
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
                Modifier.padding(all = 20.dp),
                shape = RoundedCornerShape(30.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colors.background,
                    unfocusedContainerColor = MaterialTheme.colors.background,
                    focusedBorderColor = MaterialTheme.colors.background,
                    unfocusedBorderColor = MaterialTheme.colors.background
                ),
            )

            if(uiState.invite_message.equals("")){
                Button(onClick = { viewModel.joinByInvite() },
                    shape = CircleShape,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(vertical = 10.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)) {
                    Text(text = stringResource(R.string.project_joininvite), color = MaterialTheme.colors.background,
                        fontSize = 18.sp, fontWeight = FontWeight.Medium)
                }
            }else {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(all = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically){
                    Text(text = uiState.invite_message, color = MaterialTheme.colors.background)
                }

                Button(onClick = { viewModel.openInviteWindow() },
                    shape = CircleShape,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(vertical = 10.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)) {
                    Text(text = stringResource(R.string.projectedit_close), color = MaterialTheme.colors.background,
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
                .background(MaterialTheme.colors.primary),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start) {

            OutlinedTextField(
                value = uiState.newProjectName,
                onValueChange = {viewModel.updateProjectName(it)},
                Modifier.padding(start = 20.dp, top = 20.dp),
                shape = RoundedCornerShape(30.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colors.background, unfocusedContainerColor = MaterialTheme.colors.background,
                    focusedBorderColor = MaterialTheme.colors.background, unfocusedBorderColor = MaterialTheme.colors.background),
                placeholder = {Text(text = stringResource(R.string.project_name), color = MaterialTheme.colors.surface)})

            OutlinedTextField(value = uiState.newProjectDescription,
                onValueChange = {viewModel.updateProjectDescription(it)},
                Modifier
                    .fillMaxHeight(0.75f)
                    .padding(start = 20.dp, top = 20.dp, bottom = 20.dp),
                shape = RoundedCornerShape(30.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colors.background, unfocusedContainerColor = MaterialTheme.colors.background,
                    focusedBorderColor = MaterialTheme.colors.background, unfocusedBorderColor = MaterialTheme.colors.background),
                placeholder = {Text(text = stringResource(R.string.project_description), color = MaterialTheme.colors.surface)})

            Row(Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center){
                Button(onClick = { viewModel.addProject()},
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .border(3.dp, MaterialTheme.colors.background, RoundedCornerShape(30.dp)),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)) {
                    Text(text = stringResource(R.string.project_add), color = MaterialTheme.colors.background,
                        fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}