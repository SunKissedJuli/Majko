package com.coolgirl.majko.Screen.Archive

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.coolgirl.majko.R
import com.coolgirl.majko.commons.ProjectCard
import com.coolgirl.majko.navigation.BottomBar
import com.coolgirl.majko.navigation.BottomBarScreens
import com.coolgirl.majko.navigation.ModalNavigationDrawerScreens

@Composable
fun ArchiveScreen(navController: NavHostController){

    val viewModel: ArchiveViewModel = viewModel(
        key = "archiveViewModel",
        factory = ArchiveViewModelProvider.getInstance(LocalContext.current)
    )

    val uiState by viewModel.uiState.collectAsState()

    Box(
        Modifier
            .fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {

            Column(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.93f)) {
                SetArchiveScreen(uiState, navController)
            }
            BottomBar(navController, listOf(BottomBarScreens.Notifications, BottomBarScreens.Task, BottomBarScreens.Profile))
        }

        com.coolgirl.majko.navigation.ModalNavigationDrawer(
            navController, listOf(
                ModalNavigationDrawerScreens.Task, ModalNavigationDrawerScreens.Project,
                ModalNavigationDrawerScreens.Profile, ModalNavigationDrawerScreens.Archive)
        )
    }
}

@Composable
fun SetArchiveScreen(uiState: ArchiveUiState, navController: NavHostController) {
    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top) {
        Row(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.1f)
                .padding(10.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(color = colorResource(R.color.blue))) {
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