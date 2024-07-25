package com.coolgirl.majko.Screen.ProjectEdit

import android.app.DatePickerDialog
import com.coolgirl.majko.R
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.coolgirl.majko.Screen.TaskEditor.TaskEditorViewModel
import com.coolgirl.majko.components.HorizontalLine
import com.coolgirl.majko.components.SpinnerSample
import com.coolgirl.majko.components.TaskCard
import com.coolgirl.majko.data.dataStore.UserDataStore
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun ProjectEditScreen(navController: NavHostController, project_id : String){

    val viewModel = getViewModel<ProjectEditViewModel>(
        parameters = { parametersOf(project_id) }
    )
    val uiState by viewModel.uiState.collectAsState()
    SetProjectEditScreen(uiState, viewModel, navController)
}

@Composable
fun SetProjectEditScreen(uiState: ProjectEditUiState, viewModel: ProjectEditViewModel, navController: NavHostController){
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colors.background)) {
        Row(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.08f)
                .background(MaterialTheme.colors.primary)
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                modifier = Modifier.clickable { viewModel.saveProject(navController) },
                text = stringResource(R.string.common_back), fontWeight = FontWeight.Medium,
                color = MaterialTheme.colors.background, fontSize = 50.sp,)

            var expanded by remember { mutableStateOf(false) }

            Box() { IconButton(onClick = { expanded = true }) {
                Icon(Icons.Default.MoreVert, tint = MaterialTheme.colors.background, contentDescription = "") }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth(0.5f)) {
                    Text(  stringResource(R.string.project_delite), fontSize=18.sp,
                        modifier = Modifier
                            .padding(all = 10.dp)
                            .clickable { viewModel.removeProject(navController) })
                    Text(
                        stringResource(R.string.project_createinvite), fontSize=18.sp,
                        modifier = Modifier
                            .padding(all = 10.dp)
                            .clickable { viewModel.createInvite() })
                }
            }
        }

        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.25f)) {
            uiState.projectData?.let {
                BasicTextField(
                    value = it.name,
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 15.dp),
                    textStyle = TextStyle.Default.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                    onValueChange = { viewModel.updateProjectName(it) },
                    maxLines = 2,
                    decorationBox = { innerTextField ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            if (uiState.projectData!!.name.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.project_name),
                                    color = MaterialTheme.colors.surface, fontSize = 20.sp)
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
                                Text(text = stringResource(R.string.project_description),
                                    color = MaterialTheme.colors.surface, fontSize = 18.sp)
                            }
                            innerTextField()
                        }
                    }
                )
            }
        }
        //отображение тасков, добавленных в проект
        LazyRow(
            Modifier
                .fillMaxWidth()
                .padding(all = 5.dp)) {
            if(uiState.projectData!=null){
                if(uiState.projectData.tasks!=null){
                    val projectData = uiState.projectData.tasks
                    val count = uiState.projectData?.tasks?.size?:0
                    items(count) { rowIndex ->
                        Column(Modifier.width(200.dp)) {
                            TaskCard(navController, viewModel.getPriority(projectData[rowIndex].priority),
                                viewModel.getStatusName(projectData[rowIndex].status), projectData[rowIndex], {}, {})

                        }
                    }
                }
            }
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
            Button(onClick = { viewModel.addingTask() },
                shape = CircleShape,
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .padding(vertical = 10.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)) {
                Text(text = stringResource(R.string.projectedit_addtask), color = MaterialTheme.colors.background,
                    fontSize = 18.sp, fontWeight = FontWeight.Medium)
            }
        }

        if(uiState.is_adding){
            Column(
                Modifier
                    .padding(all = 15.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(color = colorResource(R.color.purple)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top) {
                Column(
                    Modifier
                        .padding(all = 15.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(color = colorResource(R.color.white)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top) {

                    BasicTextField(
                        value = uiState.taskName,
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 15.dp)
                            .fillMaxHeight(0.09f),
                        textStyle = TextStyle.Default.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                        onValueChange = {viewModel.updateTaskName(it)},
                        maxLines = 2,
                        decorationBox = { innerTextField ->
                            Row(modifier = Modifier.fillMaxWidth()) {
                                if (uiState.taskName.isEmpty()) {
                                    Text(text = stringResource(R.string.taskeditor_name),
                                        color = MaterialTheme.colors.surface,fontSize = 20.sp) }
                                innerTextField() } })
                    BasicTextField(
                        value = uiState.taskText,
                        modifier = Modifier  .padding(horizontal = 20.dp),
                        textStyle = TextStyle.Default.copy(fontSize = 18.sp),
                        onValueChange = {viewModel.updateTaskText(it)},
                        decorationBox = { innerTextField ->
                            Row(modifier = Modifier.fillMaxWidth()) {
                                if (uiState.taskText.isEmpty()) {
                                    Text(text = stringResource(R.string.taskeditor_hint),
                                        color = MaterialTheme.colors.surface,fontSize = 18.sp) }
                                innerTextField()
                            }
                        }
                    )
                }
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, top = 5.dp, end = 15.dp, bottom = 15.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(color = colorResource(R.color.white)),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top) {
                    val mCalendar = Calendar.getInstance()
                    val mYear: Int = mCalendar.get(Calendar.YEAR)
                    val mMonth: Int = mCalendar.get(Calendar.MONTH)
                    val mDay: Int = mCalendar.get(Calendar.DAY_OF_MONTH)
                    val currentDate = Date()
                    val currentCalendar = Calendar.getInstance()
                    currentCalendar.time = currentDate

                    val mDatePickerDialog = DatePickerDialog(
                        LocalContext.current,
                        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                            val formattedData = "${year}-${month+1}-$dayOfMonth ${String.format("%02d", mCalendar.get(
                                Calendar.HOUR_OF_DAY))}:${String.format("%02d", mCalendar.get(Calendar.MINUTE))}:${String.format("%02d", mCalendar.get(
                                Calendar.SECOND))}"
                            viewModel.updateTaskDeadlie(formattedData)
                        }, mYear, mMonth, mDay)

                    Column(Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {

                        var formattedData : String = ""
                        if(uiState.taskDeadline!="") {
                            val dateTime = LocalDateTime.parse(uiState.taskDeadline, DateTimeFormatter.ofPattern("yyyy-M-d H:m:s"))
                            formattedData = dateTime.dayOfWeek.getDisplayName(java.time.format.TextStyle.SHORT, Locale("ru")) +
                                    ", " + dateTime.dayOfMonth + " "+
                                    dateTime.month.getDisplayName(java.time.format.TextStyle.FULL, Locale("ru"))
                        }
                        Text(
                            text = stringResource(R.string.taskeditor_deadline) + " " + formattedData,
                            fontSize = 18.sp,
                            modifier = Modifier.clickable { mDatePickerDialog.show() }
                        )

                        HorizontalLine()
                        SpinnerSample(name = stringResource(R.string.taskeditor_priority),
                            items = viewModel.getPriority(),
                            selectedItem = viewModel.getPriorityName(uiState.taskPriority),
                            {viewModel.updateTaskPriority(it)}
                        )
                        HorizontalLine()
                        Text(text= stringResource(R.string.taskeditor_project) + (" ") + (uiState.projectData?.name ?: stringResource(R.string.common_no)), fontSize = 18.sp)
                        HorizontalLine()
                        SpinnerSample(
                            name = stringResource(R.string.taskeditor_status),
                            items = viewModel.getStatus(),
                            selectedItem = viewModel.getStatusName(uiState.taskStatus),
                            {viewModel.updateTaskStatus(it) }
                        )

                        HorizontalLine()
                    }
                }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 30.dp),
                    horizontalArrangement = Arrangement.Center){
                    Button(onClick = { viewModel.saveTask()},
                        shape = CircleShape,
                        modifier = Modifier
                            .fillMaxWidth(0.65f)
                            .padding(vertical = 10.dp),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)) {
                        Text(text = stringResource(R.string.project_add), color = MaterialTheme.colors.background,
                            fontSize = 18.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }

        //мемберы

        if(uiState.members!=null){
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(top = 20.dp)
                    .clip(RoundedCornerShape(25.dp, 25.dp))
                    .background(color = MaterialTheme.colors.secondary),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top) {

                Spacer(modifier = Modifier.height(20.dp))

                Text(text = stringResource(R.string.projectedit_members),
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp
                )

                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp)) {
                    for(item in uiState.members!!)
                        Row(verticalAlignment = Alignment.CenterVertically){

                            Column() {
                                Text(
                                    text = stringResource(R.string.common_dash),
                                    fontSize = 55.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colors.background
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))

                            Column() {
                                Text(text = stringResource(R.string.groupeditor_name) + " " + item.user.name)
                                Text(text = stringResource(R.string.groupeditor_role) + " " + item.role_id.name)
                            }
                        }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }

    if(uiState.isInvite){
        SetInviteWindow(uiState, viewModel, { viewModel.newInvite()})
    }
}

@Composable
fun SetInviteWindow(uiState: ProjectEditUiState, viewModel : ProjectEditViewModel, onDismissRequest: () -> Unit){
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

