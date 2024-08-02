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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.coolgirl.majko.R
import com.coolgirl.majko.components.*
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProjectScreen(navController: NavHostController){

    val viewModel:ProjectViewModel  = koinViewModel()

    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit){
        coroutineScope.launch {
            viewModel.loadData()
        }
    }

    val uiState by viewModel.uiState.collectAsState()
    val uiStateCard by viewModel.uiStateCard.collectAsState()

    var expanded by remember { mutableStateOf(false) }
    var expandedFilter by remember { mutableStateOf(false) }

    //экран добавления проекта
    if(uiState.isAdding){
        AddProject(uiState, viewModel::updateProjectName, viewModel::updateProjectDescription,
            viewModel::addProject, viewModel::notAddingProjectYet)
    }

    //экран для ввода инвайта
    if(uiState.isInvite){
        JoinByInviteWindow(uiState, viewModel::updateInvite,
            viewModel::joinByInvite, viewModel::openInviteWindow)
    }

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

    Scaffold(
        topBar = {

            //панель при длинном нажатии
            if (uiState.isLongtap) {
                LongTapPanel({viewModel.toArchive()}, {viewModel.removeProjects()})
            } else {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(65.dp)
                        .padding(all = 10.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .background(color = MaterialTheme.colors.primary),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    SearchBox(uiState.searchString, { viewModel.updateSearchString(it, 2) }, R.string.project_search)

                    Column {
                        Row {
                            IconButton(onClick = { expandedFilter = !expandedFilter }, Modifier.size(27.dp)) {
                                Icon(painter = painterResource(R.drawable.icon_filter),
                                    contentDescription = "", tint = MaterialTheme.colors.background)
                            }
                        }
                        FilterDropdown(expanded = expandedFilter,
                            onDismissRequest = { expandedFilter = it }, R.string.filter_project_group,
                            { viewModel.updateSearchString(uiState.searchString, it) },
                            R.string.filter_group_personal, R.string.filter_all)
                    }

                    Spacer(modifier = Modifier.width(5.dp))

                    IconButton(onClick = { viewModel.updateSearchString(uiState.searchString, 2) }, Modifier.size(27.dp)) {
                        Icon(painter = painterResource(R.drawable.icon_filter_off),
                            contentDescription = "", tint = MaterialTheme.colors.background)
                    }

                    Box(Modifier.padding(end = 10.dp)) {
                        IconButton(onClick = { expanded = true }) {
                            Icon(painter = painterResource(R.drawable.icon_menu),
                                contentDescription = "", tint = MaterialTheme.colors.background)
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.fillMaxWidth(0.5f)
                        ) {

                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.openInviteWindow()
                                        expanded = false}) {
                                Text(stringResource(R.string.project_joininvite),
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(all = 10.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(it)) {
            Column(Modifier.fillMaxSize()) {
                SetProjectScreen(uiState, navController, viewModel, uiStateCard)
            }

            Box(Modifier.align(Alignment.BottomEnd)){
                AddButton(onClick = {viewModel.addingProject()}, id = "")
            }
        }
    }

}

@Composable
fun SetProjectScreen(uiState: ProjectUiState, navController: NavHostController, viewModel: ProjectViewModel, uiStateCard: ProjectCardUiState) {
    val personalProject = uiState.searchPersonalProject
    val groupProject = uiState.searchGroupProject

    Column(Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {

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
fun JoinByInviteWindow(uiState: ProjectUiState, onUpdate: (String) -> Unit,
                       onJoin: () -> Unit, onDismissRequest: () -> Unit){
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(MaterialTheme.colors.secondary)) {

            WhiteRoundedTextField(uiState.invite, onUpdate,
                stringResource(R.string.invite), Modifier.padding(bottom = 20.dp))

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(all = 10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically){
                if(uiState.invite_message.equals("")){
                    BlueRoundedButton(onJoin, stringResource(R.string.project_joininvite))
                }else {
                    Text(text = uiState.invite_message, color = MaterialTheme.colors.background)
                    BlueRoundedButton(onDismissRequest, stringResource(R.string.projectedit_close))
                }
            }
        }
    }
}

@Composable
fun AddProject(uiState: ProjectUiState,  onUpdateName: (String) -> Unit,
               onUpdateText: (String) -> Unit, onAdding:()->Unit, onDismissRequest: () -> Unit){
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp)
                .padding(16.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(MaterialTheme.colors.secondary)) {

            WhiteRoundedTextField(uiState.newProjectName, onUpdateName,
                stringResource(R.string.project_name) )

            WhiteRoundedTextField(uiState.newProjectDescription, onUpdateText,
                stringResource(R.string.project_description),
                Modifier
                    .fillMaxHeight(0.75f)
                    .padding(bottom = 20.dp))

            Row(Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center){
                BlueRoundedButton(onAdding, stringResource(R.string.project_add))
            }
        }
    }
}

@Composable
fun LongTapPanel(onAddingToArchive: ()-> Unit, onRemoving: ()-> Unit){
    var expandedLongPanel by remember { mutableStateOf(false) }
    Row(
        Modifier
            .fillMaxWidth()
            .height(65.dp)
            .background(color = MaterialTheme.colors.secondary),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End){


        Box(Modifier.padding(all = 10.dp)) {

            IconButton(onClick = {expandedLongPanel = true  }) {
                Icon(painter = painterResource(R.drawable.icon_menu),
                    contentDescription = "", tint = MaterialTheme.colors.background)
            }

            DropdownMenu(
                expanded = expandedLongPanel,
                onDismissRequest = { expandedLongPanel = false },
                modifier = Modifier.fillMaxWidth(0.5f)) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            onAddingToArchive
                            expandedLongPanel = false
                        }) {
                    Text(stringResource(R.string.project_to_archive), fontSize = 18.sp, modifier = Modifier.padding(all = 10.dp))
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            onRemoving
                            expandedLongPanel = false
                        }) {
                    Text(stringResource(R.string.project_delite), fontSize = 18.sp, modifier = Modifier.padding(all = 10.dp))
                }
            }
        }
    }
}