package com.coolgirl.majko.Screen.Archive

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.coolgirl.majko.R
import com.coolgirl.majko.components.*
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ArchiveScreen(navController: NavHostController){

    val viewModel: ArchiveViewModel = koinViewModel()

    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit){
        coroutineScope.launch {
            viewModel.loadData()
        }
    }

    val uiState by viewModel.uiState.collectAsState()
    val uiStateCard by viewModel.uiStateCard.collectAsState()

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
                LongTapPanel({ viewModel.fromArchive() }, { viewModel.removeProjects() })
            }
            else {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(65.dp)
                        .padding(all = 10.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .background(color = MaterialTheme.colorScheme.primary),
                    verticalAlignment = Alignment.CenterVertically) {

                    SearchBox(uiState.searchString, { viewModel.updateSearchString(it, 2) }, R.string.project_search)
                    Column {
                        Row {
                            IconButton(onClick = { viewModel.updateExpandedFilter() }, Modifier.size(27.dp)) {
                                Icon(painter = painterResource(R.drawable.icon_filter),
                                    contentDescription = "", tint = MaterialTheme.colorScheme.background)
                            }

                        }
                        FilterDropdown(expanded = uiState.expandedFilter, onDismissRequest = { viewModel.updateExpandedFilter() },
                            R.string.filter_project_group, { viewModel.updateSearchString(uiState.searchString, it) },
                            R.string.filter_group_personal, R.string.filter_all,)
                    }
                    Spacer(modifier = Modifier.width(5.dp))

                    IconButton(onClick = { viewModel.updateSearchString(uiState.searchString, 2) }, Modifier.size(27.dp)) {
                        Icon(painter = painterResource(R.drawable.icon_filter_off),
                            contentDescription = "", tint = MaterialTheme.colorScheme.background)
                    }
                }
            }
        }
    ) {
        Column(Modifier
                .fillMaxSize()
                .padding(it)
            .background(MaterialTheme.colorScheme.background)) {
            SetArchiveScreen(uiState, navController, viewModel, uiStateCard)
        }
    }
}

@Composable
fun SetArchiveScreen(uiState: ArchiveUiState, navController: NavHostController, viewModel: ArchiveViewModel, uiStateCard: ProjectCardUiState) {
    val personalProject = uiState.searchPersonalProject
    val groupProject = uiState.searchGroupProject

    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top) {
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (!groupProject.isNullOrEmpty()) {
                item {
                    Text(text = stringResource(R.string.project_group),
                        color = MaterialTheme.colorScheme.onSurface,
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
                        color = MaterialTheme.colorScheme.onSurface,
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
private fun LongTapPanel(onRemovingFromArchive: ()-> Unit, onRemoving: ()-> Unit){
    var expandedLongPanel by remember { mutableStateOf(false) }
    Row(
        Modifier
            .fillMaxWidth()
            .height(65.dp)
            .background(color = MaterialTheme.colorScheme.secondary),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End){


        Box(Modifier.padding(all = 10.dp)) {
            IconButton(onClick = {expandedLongPanel = true  }) {
                Icon(painter = painterResource(R.drawable.icon_menu),
                    contentDescription = "", tint = MaterialTheme.colorScheme.background)
            }
            DropdownMenu(
                expanded = expandedLongPanel,
                onDismissRequest = { expandedLongPanel = false },
                modifier = Modifier.fillMaxWidth(0.5f)) {

                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            onRemovingFromArchive
                            expandedLongPanel = false
                        }){
                    Text(stringResource(R.string.archive_to_project), fontSize = 18.sp, modifier = Modifier.padding(all = 10.dp))
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            onRemoving
                            expandedLongPanel = false
                        }){
                    Text(stringResource(R.string.project_delite), fontSize = 18.sp, modifier = Modifier.padding(all = 10.dp))
                }
            }
        }
    }
}