package com.coolgirl.majko.Sc

import androidx.compose.foundation.*
import com.coolgirl.majko.Screen.GroupEditor.GroupEditorUiState
import com.coolgirl.majko.Screen.GroupEditor.GroupEditorViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.coolgirl.majko.R
import com.coolgirl.majko.Screen.ProjectEdit.ProjectEditUiState
import com.coolgirl.majko.Screen.ProjectEdit.ProjectEditViewModel
import com.coolgirl.majko.Screen.TaskEditor.TaskEditorViewModel
import com.coolgirl.majko.components.ButtonBack
import com.coolgirl.majko.components.ProjectCard
import com.coolgirl.majko.data.dataStore.UserDataStore
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getKoin
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject
@Composable
fun GroupEditorScreen(navController: NavHostController, groupId: String){

    val viewModel: GroupEditorViewModel = koinViewModel()

    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit){
        coroutineScope.launch {
            viewModel.loadData(groupId)
        }
    }

    val uiState by viewModel.uiState.collectAsState()
    SetGroupEditorScreen(uiState, viewModel, navController)
}

@Composable
fun SetGroupEditorScreen(uiState: GroupEditorUiState, viewModel: GroupEditorViewModel, navController: NavHostController){
    var expanded by remember { mutableStateOf(false) }

    if(uiState.isInvite){
        SetInviteWindow(uiState, viewModel, { viewModel.newInvite()})
    }

    if(uiState.isMembers){
        SetMembersWindow(uiState, { viewModel.showMembers()})
    }

    Scaffold(
        topBar = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.08f)
                    .background(MaterialTheme.colors.primary)
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                ButtonBack({viewModel.saveGroup(navController)})

                Box() {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            Icons.Default.MoreVert,
                            tint = MaterialTheme.colors.background,
                            contentDescription = ""
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth(0.5f)
                    ) {
                        if (!uiState.members.isNullOrEmpty()) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.showMembers() }) {
                                Text(
                                    stringResource(R.string.projectedit_showmembers),
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(all = 10.dp)
                                )
                            }
                        }
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.removeGroup(navController) }) {
                            Text(
                                stringResource(R.string.project_delite), fontSize = 18.sp,
                                modifier = Modifier.padding(all = 10.dp)
                            )
                        }
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.createInvite() }) {
                            Text(
                                stringResource(R.string.project_createinvite), fontSize = 18.sp,
                                modifier = Modifier
                                    .padding(all = 10.dp)
                            )
                        }
                    }

                }
            }
        }
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colors.background)
                .padding(it)) {

            Column(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.25f)) {
                uiState.groupData?.let {
                    BasicTextField(
                        value = it.title,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 15.dp),
                        textStyle = TextStyle.Default.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                        onValueChange = { viewModel.updateGroupName(it) },
                        decorationBox = { innerTextField ->
                            Row(modifier = Modifier.fillMaxWidth()) {
                                if (uiState.groupData!!.title.isEmpty()) {
                                    Text(
                                        text = stringResource(R.string.group_name),
                                        color = MaterialTheme.colors.surface, fontSize = 20.sp)
                                }
                                innerTextField()
                            }
                        }
                    )
                }

                uiState.groupData?.let {
                    BasicTextField(
                        value = it.description,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxHeight(),
                        textStyle = TextStyle.Default.copy(fontSize = 18.sp),
                        onValueChange = { viewModel.updateGroupDescription(it) },
                        decorationBox = { innerTextField ->
                            Row(modifier = Modifier.fillMaxWidth()) {
                                if (uiState.groupData!!.description.isEmpty()) {
                                    Text(text = stringResource(R.string.group_description),
                                        color = MaterialTheme.colors.surface, fontSize = 18.sp)
                                }
                                innerTextField()
                            }
                        }
                    )
                }
            }

            //отображение пректов, добавленных в группу
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(all = 5.dp)) {
                if(uiState.groupData!=null){
                    if(!uiState.groupData.projectsGroup.isNullOrEmpty()){
                        val projectData = uiState.groupData.projectsGroup

                        for (item in projectData){
                            ProjectCard(navController, projectData = item, onLongTap = {}, onLongTapRelease =  {}, isSelected = false)
                        }
                    }
                }
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                Button(onClick = { viewModel.addingProject() },
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.65f)
                        .padding(vertical = 10.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)) {
                    Text(text = stringResource(R.string.groupeditor_addproject), color = MaterialTheme.colors.background,
                        fontSize = 18.sp, fontWeight = FontWeight.Medium)
                }
            }

            //добавление проекта в группу
            if(uiState.isAdding){
                LazyRow(
                    Modifier
                        .fillMaxWidth()
                        .padding(all = 5.dp)) {
                    if(!uiState.projectData.isNullOrEmpty()){
                        val projectData = uiState.projectData
                        val count = uiState.projectData.size?:0
                        items(count) { rowIndex ->
                            Column(Modifier.width(200.dp)) {
                                ProjectCard(navController, projectData = projectData[rowIndex], onLongTap = {}, onLongTapRelease =  {}, isSelected = false)
                                Row(Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center){
                                    Button(onClick = { viewModel.saveProject(projectData[rowIndex].id) },
                                        shape = RoundedCornerShape(15.dp),
                                        modifier = Modifier
                                            .fillMaxWidth(0.8f)
                                            .padding(vertical = 10.dp),
                                        colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)) {
                                        Text(text = stringResource(R.string.project_add), color = MaterialTheme.colors.background,
                                            fontSize = 18.sp, fontWeight = FontWeight.Medium)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SetMembersWindow(uiState: GroupEditorUiState, onDismissRequest: () -> Unit){
    if (!uiState.members.isNullOrEmpty()) {
        Dialog(onDismissRequest = { onDismissRequest() }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(MaterialTheme.colors.secondary)
            ) {
                Column(Modifier.padding(start = 15.dp)) {
                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = stringResource(R.string.projectedit_members),
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Column(
                        Modifier
                            .padding(
                                horizontal = 20.dp,
                                vertical = 10.dp
                            )
                    ) {
                        uiState.members?.forEach { item ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 5.dp)
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.icon_line),
                                    contentDescription = "",
                                    tint = MaterialTheme.colors.background
                                )
                                Spacer(modifier = Modifier.width(10.dp))

                                Column {
                                    Text(text = stringResource(R.string.groupeditor_name) + " " + item.user.name)
                                    Text(text = stringResource(R.string.groupeditor_role) + " " + item.role.name)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    else{
        Dialog(onDismissRequest = { onDismissRequest() }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(MaterialTheme.colors.secondary)
            ) {
                Text(text = stringResource(R.string.projectedit_membersempty), color = MaterialTheme.colors.background,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontSize = 18.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
private fun SetInviteWindow(uiState: GroupEditorUiState, viewModel : GroupEditorViewModel, onDismissRequest: () -> Unit){
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(MaterialTheme.colors.secondary)) {

            OutlinedTextField(
                value = uiState.invite,
                onValueChange = { },
                Modifier.padding(all = 20.dp),
                enabled = true,
                shape = RoundedCornerShape(30.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colors.background,
                    unfocusedContainerColor = MaterialTheme.colors.background,
                    focusedBorderColor = MaterialTheme.colors.background,
                    unfocusedBorderColor = MaterialTheme.colors.background
                ),
            )

            Button(onClick = { viewModel.newInvite()},
                shape = CircleShape,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(vertical = 10.dp)
                    .align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)) {
                Text(text = stringResource(R.string.projectedit_close), color = MaterialTheme.colors.background,
                    fontSize = 18.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

