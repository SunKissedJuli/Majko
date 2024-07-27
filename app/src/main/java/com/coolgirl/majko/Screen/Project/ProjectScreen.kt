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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.coolgirl.majko.R
import com.coolgirl.majko.components.*
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@Composable
fun ProjectScreen(navController: NavHostController){

    val viewModel = getViewModel<ProjectViewModel>()

    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit){
        coroutineScope.launch {
            viewModel.loadData()
        }
    }

    val uiState by viewModel.uiState.collectAsState()
    val uiStateCard by viewModel.uiStateCard.collectAsState()

    Box(
        Modifier
            .fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            SetProjectScreen(uiState, navController, viewModel, uiStateCard)
        }

        Box(Modifier.align(Alignment.BottomEnd)){
            plusButton(onClick = {viewModel.addingProject()}, id = "")
        }

        //панель при длинном нажатии
        if(uiState.isLongtap){
            Row(
                Modifier
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
                        Row(Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.toArchive()
                            expanded = false }) {
                            Text(
                                stringResource(R.string.project_to_archive),
                                fontSize = 18.sp,
                                modifier = Modifier.padding(all = 10.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    //экран добавления проекта
    if(uiState.isAdding){
        AddProject(uiState, viewModel, { viewModel.notAddingProjectYet()})
    }

    if(uiState.isInvite){
        JoinByInviteWindow(uiState, viewModel, { viewModel.openInviteWindow()})
    }


}

@Composable
fun SetProjectScreen(uiState: ProjectUiState, navController: NavHostController, viewModel: ProjectViewModel, uiStateCard: ProjectCardUiState) {
    var expanded by remember { mutableStateOf(false) }
    var expandedFilter by remember { mutableStateOf(false) }

    val personalProject = uiState.searchPersonalProject
    val groupProject = uiState.searchGroupProject

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

            SearchBox(uiState.searchString, {viewModel.updateSearchString(it, 2)}, R.string.project_search )
            Column {
                Row {
                    Icon(painter = painterResource(R.drawable.icon_filter),
                        modifier = Modifier.clickable {expandedFilter = !expandedFilter },
                        contentDescription = "",
                        tint = MaterialTheme.colors.surface)
                }
                FilterDropdown(expanded = expandedFilter, onDismissRequest = { expandedFilter = it },
                    R.string.filter_project_group, { viewModel.updateSearchString(uiState.searchString, 1) },
                    R.string.filter_group_personal, {viewModel.updateSearchString(uiState.searchString, 0)},
                    R.string.filter_all, {viewModel.updateSearchString(uiState.searchString, 2)})
            }

            Spacer(modifier = Modifier.width(5.dp))

            Icon(painter = painterResource(R.drawable.icon_filter_off),
                modifier = Modifier.clickable { viewModel.updateSearchString(uiState.searchString, 2) },
                contentDescription = "", tint = MaterialTheme.colors.surface)


            Box(Modifier.padding(end = 10.dp)) {
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

                    Row(Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.openInviteWindow() }) {
                        Text(
                            stringResource(R.string.project_joininvite),
                            fontSize = 18.sp,
                            modifier = Modifier
                                .padding(all = 10.dp)
                        )
                    }
                }
            }
        }

        LazyColumn(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (!groupProject.isNullOrEmpty()) {
                item {
                    Text(text = stringResource(R.string.project_group),
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier.padding(start = 15.dp, top = 10.dp, bottom = 10.dp))
                }
                items(groupProject) { project ->
                    ProjectCard(
                        navController,
                        projectData = project,
                        onLongTap = { viewModel.openPanel(it) },
                        onLongTapRelease = { viewModel.openPanel(it) },
                        isSelected = uiState.longtapProjectId.contains(project.id)
                    )
                }
            }

            if (!personalProject.isNullOrEmpty()) {
                item {
                    Text(text = stringResource(R.string.project_personal),
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier.padding(start = 15.dp, top = 10.dp, bottom = 10.dp))
                }
                items(personalProject) { project ->
                    ProjectCard(
                        navController,
                        projectData = project,
                        onLongTap = { viewModel.openPanel(it) },
                        onLongTapRelease = { viewModel.openPanel(it) },
                        isSelected = uiState.longtapProjectId.contains(project.id)
                    )
                }
            }
        }
    }
}

@Composable
fun JoinByInviteWindow(uiState: ProjectUiState, viewModel: ProjectViewModel, onDismissRequest: () -> Unit){
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(MaterialTheme.colors.secondary)) {

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
                        .fillMaxWidth(0.6f)
                        .padding(vertical = 10.dp)
                        .align(Alignment.CenterHorizontally),
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
                        .fillMaxWidth(0.6f)
                        .padding(vertical = 10.dp)
                        .align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)) {
                    Text(text = stringResource(R.string.projectedit_close), color = MaterialTheme.colors.background,
                        fontSize = 18.sp, fontWeight = FontWeight.Medium)
                }
            }


        }
    }
}

@Composable
fun AddProject(uiState: ProjectUiState, viewModel: ProjectViewModel, onDismissRequest: () -> Unit){
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp)
                .padding(16.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(MaterialTheme.colors.secondary)) {

            OutlinedTextField(
                value = uiState.newProjectName,
                onValueChange = {viewModel.updateProjectName(it)},
                Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp),
                shape = RoundedCornerShape(30.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colors.background, unfocusedContainerColor = MaterialTheme.colors.background,
                    focusedBorderColor = MaterialTheme.colors.background, unfocusedBorderColor = MaterialTheme.colors.background),
                placeholder = {Text(text = stringResource(R.string.project_name), color = MaterialTheme.colors.onSurface)})

            OutlinedTextField(value = uiState.newProjectDescription,
                onValueChange = {viewModel.updateProjectDescription(it)},
                Modifier
                    .fillMaxHeight(0.75f)
                    .padding(start = 20.dp, top = 20.dp, bottom = 20.dp, end = 20.dp),
                shape = RoundedCornerShape(30.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colors.background, unfocusedContainerColor = MaterialTheme.colors.background,
                    focusedBorderColor = MaterialTheme.colors.background, unfocusedBorderColor = MaterialTheme.colors.background),
                placeholder = {Text(text = stringResource(R.string.project_description), color = MaterialTheme.colors.onSurface)})

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