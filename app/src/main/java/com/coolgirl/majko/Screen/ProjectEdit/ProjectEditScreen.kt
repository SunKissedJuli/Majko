package com.coolgirl.majko.Screen.ProjectEdit

import android.app.DatePickerDialog
import com.coolgirl.majko.R
import android.widget.DatePicker
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.coolgirl.majko.components.*
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun ProjectEditScreen(navController: NavHostController, projectId : String){

    val viewModel: ProjectEditViewModel = koinViewModel()

    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit){
        coroutineScope.launch {
            viewModel.loadData(projectId)
        }
    }

    val uiState by viewModel.uiState.collectAsState()

    BackHandler { viewModel.updateExitDialog() }

    if (uiState.exitDialog) {
        ExitAlertDialog(
            onConfirm = { viewModel.updateExitDialog()
                viewModel.saveProject(navController) },
            onDismiss = { viewModel.updateExitDialog()
                navController.popBackStack()})
    }

    SetProjectEditScreen(uiState, viewModel, navController)
}

@Composable
fun SetProjectEditScreen(uiState: ProjectEditUiState, viewModel: ProjectEditViewModel, navController: NavHostController) {
    if(uiState.isMembers){
        SetMembersWindow(uiState, viewModel::showMembers)
    }

    if (uiState.isInvite) {
        SetInviteWindow(uiState, viewModel::newInvite)
    }

    if (uiState.isAdding) {
        addTask(uiState, viewModel, viewModel::addingTask)
    }

    Scaffold(
        topBar =  {
           Row(
               Modifier
                   .fillMaxWidth()
                   .height(60.dp)
                   .background(MaterialTheme.colors.primary)
                   .padding(horizontal = 10.dp),
               verticalAlignment = Alignment.CenterVertically,
               horizontalArrangement = Arrangement.SpaceBetween) {

               ButtonBack({viewModel.saveProject(navController)})

               Box {
                   IconButton(onClick = {viewModel.updateExpanded() }) {
                       Icon(painter = painterResource(R.drawable.icon_menu),
                           contentDescription = "", tint = MaterialTheme.colors.background)
                   }
                   DropdownMenu(
                       expanded = uiState.expanded,
                       onDismissRequest = { viewModel.updateExpanded() },
                       modifier = Modifier.fillMaxWidth(0.5f)
                   ) {
                       if(!uiState.members.isNullOrEmpty()){
                           Row(
                               Modifier
                                   .fillMaxWidth()
                                   .clickable { viewModel.showMembers()
                                       viewModel.updateExpanded()}) {
                               Text(stringResource(R.string.projectedit_showmembers), fontSize = 18.sp, modifier = Modifier.padding(all = 10.dp))
                           }
                       }
                       Row(
                           Modifier
                               .fillMaxWidth()
                               .clickable { viewModel.removeProject(navController)
                                   viewModel.updateExpanded()}) {
                           Text(stringResource(R.string.project_delite), fontSize = 18.sp, modifier = Modifier.padding(all = 10.dp))
                       }
                       Row(
                           Modifier
                               .fillMaxWidth()
                               .clickable { viewModel.createInvite()
                                   viewModel.updateExpanded()}) {
                           Text(stringResource(R.string.project_createinvite), fontSize = 18.sp, modifier = Modifier.padding(all = 10.dp))
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
                .padding(it)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()) {
                uiState.projectData?.let {
                    BasicTextField(
                        value = it.name,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 15.dp),
                        textStyle = TextStyle.Default.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        onValueChange = { viewModel.updateProjectName(it) },
                        decorationBox = { innerTextField ->
                            Row(modifier = Modifier.fillMaxWidth()) {
                                if (uiState.projectData!!.name.isEmpty()) {
                                    Text(
                                        text = stringResource(R.string.project_name),
                                        color = MaterialTheme.colors.surface,
                                        fontSize = 20.sp
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                }

                uiState.projectData?.let {
                    BasicTextField(
                        value = it.description,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxHeight(),
                        textStyle = TextStyle.Default.copy(fontSize = 18.sp),
                        onValueChange = { viewModel.updateProjectDescription(it) },
                        decorationBox = { innerTextField ->
                            Row(modifier = Modifier.fillMaxWidth()) {
                                if (uiState.projectData!!.description.isEmpty()) {
                                    Text(
                                        text = stringResource(R.string.project_description),
                                        color = MaterialTheme.colors.surface,
                                        fontSize = 18.sp
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                }
            }

            // отображение тасков, добавленных в проект
            LazyRow(Modifier
                    .fillMaxWidth()
                    .padding(all = 5.dp)) {
                if (uiState.projectData != null) {
                    if (!uiState.projectData.tasks.isNullOrEmpty()) {
                        val projectData = uiState.projectData.tasks
                        val count = uiState.projectData.tasks.size
                        items(count) { rowIndex ->
                            Column(
                                Modifier.width(200.dp)) {
                                TaskCard(navController,
                                    viewModel.getPriority(projectData[rowIndex].priority),
                                    viewModel.getStatusName(projectData[rowIndex].status),
                                    projectData[rowIndex])
                            }
                        }
                    }
                }
            }

            Row(Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = { viewModel.addingTask() },
                    shape = CircleShape,
                    modifier = Modifier
                        .fillMaxWidth(0.65f)
                        .padding(vertical = 10.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)) {
                    Text(text = stringResource(R.string.projectedit_addtask),
                        color = MaterialTheme.colors.background,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun SetMembersWindow(uiState: ProjectEditUiState, onDismissRequest: () -> Unit){
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

                    Text(text = stringResource(R.string.projectedit_members), fontWeight = FontWeight.Medium,
                        fontSize = 18.sp, modifier = Modifier.align(Alignment.CenterHorizontally))

                    Column(Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
                        uiState.members?.forEach { item ->
                            Row(verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 5.dp)) {
                                Icon(painter = painterResource(R.drawable.icon_line),
                                    contentDescription = "", tint = MaterialTheme.colors.background)
                                Spacer(modifier = Modifier.width(10.dp))

                                Column {
                                    Text(text = stringResource(R.string.groupeditor_name) + " " + item.user.name)
                                    Text(text = stringResource(R.string.groupeditor_role) + " " + item.roleId.name)
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
private fun SetInviteWindow(uiState: ProjectEditUiState, onDismissRequest: () -> Unit){
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(MaterialTheme.colors.secondary)) {

            WhiteRoundedTextField(uiState.invite, { },
                stringResource(R.string.invite), Modifier.padding(bottom = 20.dp))

            Row(Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically){
                BlueRoundedButton(onDismissRequest, stringResource(R.string.projectedit_close),
                    modifier = Modifier.padding(bottom = 15.dp))
            }
        }
    }
}

@Composable
private fun addTask(uiState: ProjectEditUiState, viewModel: ProjectEditViewModel, onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
       Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(MaterialTheme.colors.secondary)
        ) {
            Column() {
                Column(
                    Modifier
                        .padding(all = 15.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(color = colorResource(R.color.white)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {

                    BasicTextField(
                        value = uiState.taskName,
                        modifier = Modifier
                            .padding(start = 18.dp, top = 15.dp, bottom = 5.dp)
                            .fillMaxHeight(0.09f),
                        textStyle = TextStyle.Default.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold),
                        onValueChange = { viewModel.updateTaskName(it) },
                        maxLines = 2,
                        decorationBox = { innerTextField ->
                            Row(modifier = Modifier.fillMaxWidth()) {
                                if (uiState.taskName.isEmpty()) {
                                    Text(text = stringResource(R.string.taskeditor_name),
                                        color = MaterialTheme.colors.onSurface, fontSize = 20.sp, maxLines = 2)
                                }
                                innerTextField()
                            }
                        })
                    BasicTextField(
                        value = uiState.taskText,
                        modifier = Modifier
                            .fillMaxHeight(0.25f)
                            .padding(horizontal = 18.dp),
                        textStyle = TextStyle.Default.copy(fontSize = 18.sp),
                        onValueChange = { viewModel.updateTaskText(it) },
                        decorationBox = { innerTextField ->
                            Row(modifier = Modifier.fillMaxWidth()) {
                                if (uiState.taskText.isEmpty()) {
                                    Text(text = stringResource(R.string.taskeditor_hint),
                                        color = MaterialTheme.colors.onSurface, fontSize = 18.sp, maxLines = 4)
                                }
                                innerTextField()
                            }
                        }
                    )
                }
                Column(Modifier.fillMaxWidth()
                        .padding(start = 15.dp, top = 5.dp, end = 15.dp, bottom = 15.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(color = colorResource(R.color.white)),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {

                    Column(Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {

                        DeadlineDatePicker(currentDeadline = uiState.taskDeadline,
                            onUpdateDeadline = { newDate -> viewModel.updateTaskDeadlie(newDate) })
                        HorizontalLine()

                        SpinnerSample(name = stringResource(R.string.taskeditor_priority),
                            items = viewModel.getPriority(),
                            selectedItem = viewModel.getPriorityName(uiState.taskPriority),
                            { viewModel.updateTaskPriority(it) })
                        HorizontalLine()

                        Text(text = stringResource(R.string.taskeditor_project) + (" ") + (uiState.projectData?.name
                                ?: stringResource(R.string.common_no)), fontSize = 18.sp)
                        HorizontalLine()

                        SpinnerSample(name = stringResource(R.string.taskeditor_status),
                            items = viewModel.getStatus(),
                            selectedItem = viewModel.getStatusName(uiState.taskStatus),
                            { viewModel.updateTaskStatus(it) })
                        HorizontalLine()
                    }
                }

                Row(Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp),
                    horizontalArrangement = Arrangement.Center) {
                    Button(onClick = { viewModel.saveTask() },
                        shape = CircleShape,
                        modifier = Modifier
                            .fillMaxWidth(0.65f)
                            .padding(vertical = 5.dp),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)) {
                        Text(text = stringResource(R.string.project_add), color = MaterialTheme.colors.background,
                            fontSize = 18.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

