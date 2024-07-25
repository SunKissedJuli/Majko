package com.coolgirl.majko.Screen.Archive

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.coolgirl.majko.R
import com.coolgirl.majko.Screen.Profile.ProfileViewModel
import com.coolgirl.majko.components.ProjectCard
import com.coolgirl.majko.components.ProjectCardUiState
import com.coolgirl.majko.navigation.BottomBar
import com.coolgirl.majko.navigation.BottomBarScreens
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@Composable
fun ArchiveScreen(navController: NavHostController){

    val viewModel = getViewModel<ArchiveViewModel>()

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

            Column(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.93f)) {
                SetArchiveScreen(uiState, navController, viewModel, uiStateCard)
            }
        }
    }

    //панель при длинном нажатии
    if(uiState.isLongtap){
        Row(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.1f)
                .background(color = MaterialTheme.colors.primaryVariant),
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
                        stringResource(R.string.archive_to_project),
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(all = 10.dp)
                            .clickable {
                                viewModel.fromArchive()
                            }
                    )
                }
            }
        }
    }

}

@Composable
fun SetArchiveScreen(uiState: ArchiveUiState, navController: NavHostController, viewModel: ArchiveViewModel, uiStateCard: ProjectCardUiState) {
    Column(
        Modifier.fillMaxWidth(),
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
                        color = MaterialTheme.colors.surface,
                        modifier = Modifier.padding(start = 15.dp, top = 10.dp, bottom = 10.dp))
                }
                items(groupProject) { project ->
                    ProjectCard(navController, projectData = project, is_archive = true, onLongTap = {viewModel.openPanel(it)})
                }
            }

            if (!personalProject.isNullOrEmpty()) {
                item {
                    Text(text = stringResource(R.string.project_personal),
                        color = colorResource(R.color.gray),
                        modifier = Modifier.padding(start = 15.dp, top = 10.dp, bottom = 10.dp))
                }
                items(personalProject) { project ->
                    ProjectCard(navController, projectData = project, is_archive = true, onLongTap = {viewModel.openPanel(it)})
                }
            }
        }
    }
}