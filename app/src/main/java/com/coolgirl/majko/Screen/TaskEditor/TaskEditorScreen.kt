package com.coolgirl.majko.Screen.TaskEditor

import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.coolgirl.majko.R
import com.coolgirl.majko.commons.SpinnerItems
import com.coolgirl.majko.commons.SpinnerSample
import com.coolgirl.majko.data.dataStore.UserDataStore
import java.util.*

@Composable
fun TaskEditorScreen(navController: NavHostController, task_id : String){
    val cont = LocalContext.current
    val viewModel : TaskEditorViewModel = remember{ TaskEditorViewModel(UserDataStore(cont), task_id)}
    val uiState by viewModel.uiState.collectAsState()
    SetTaskEditorScreen(uiState, {viewModel.updateTaskText(it)},
        { viewModel.updateTaskName(it) }, viewModel, navController)
}

@Composable
fun SetTaskEditorScreen(uiState: TaskEditorUiState, onUpdateTaskText: (String) -> Unit, onUpdateTaskName:(String) -> Unit, viewModel: TaskEditorViewModel, navController: NavHostController) {
    Column(
        Modifier
            .fillMaxSize()
            .background(colorResource(uiState.backgroundColor))) {
        Row(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.08f)
                .background(colorResource(R.color.blue))
                .padding(10.dp, 0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start) {
            Text(modifier = Modifier.clickable { viewModel.saveTask(navController) }, text = "←", fontWeight = FontWeight.Medium,
                color = colorResource(R.color.white), fontSize = 50.sp,
            )
            //   Text(text = noteData, fontSize = 18.sp)
        }
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.73f)) {
            BasicTextField(
                value = uiState.taskName,
                modifier = Modifier
                    .padding(20.dp, 15.dp)
                    .fillMaxHeight(0.09f),
                textStyle = TextStyle.Default.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                onValueChange = onUpdateTaskName,
                maxLines = 2,
                decorationBox = { innerTextField ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        if (uiState.taskName.isEmpty()) {
                            Text(text = stringResource(R.string.taskeditor_name),
                                color = colorResource(R.color.gray),fontSize = 20.sp) }
                        innerTextField() } })
            BasicTextField(
                value = uiState.taskText,
                modifier = Modifier.padding(20.dp, 0.dp),
                textStyle = TextStyle.Default.copy(fontSize = 18.sp),
                onValueChange = onUpdateTaskText,
                decorationBox = { innerTextField ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        if (uiState.taskText.isEmpty()) {
                            Text(text = stringResource(R.string.taskeditor_hint),
                                color = colorResource(R.color.gray),fontSize = 18.sp) }
                        innerTextField() } })
        }
        Column(Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.purple))
            .padding(20.dp)) {
            Row(){
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
                        val formattedData = "${year}-${month+1}-$dayOfMonth ${String.format("%02d", mCalendar.get(Calendar.HOUR_OF_DAY))}:${String.format("%02d", mCalendar.get(Calendar.MINUTE))}:${String.format("%02d", mCalendar.get(Calendar.SECOND))}"
                        viewModel.updateTaskDeadlie(formattedData)
                        Log.d("tag", "Taskeditor formattedData = $formattedData")
                        }, mYear, mMonth, mDay)

                Text(
                    text = stringResource(R.string.taskeditor_deadline) + " " + uiState.taskDeadline,
                    fontSize = 18.sp,
                    modifier = Modifier.clickable { mDatePickerDialog.show() }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(){
                SpinnerSample(name = stringResource(R.string.taskeditor_priority),
                    items = viewModel.getPriority(),
                    selectedItem = viewModel.getPriorityName(uiState.taskPriority),
                    {viewModel.updateTaskPriority(it)})
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(){
                SpinnerSample(name = stringResource(R.string.taskeditor_project), items = listOf(
                    SpinnerItems("1", "Item 1"),
                    SpinnerItems("2", "Item 2"),
                    SpinnerItems("3", "Item 3")),
                    selectedItem = "",
                    {viewModel.updateTaskProject(it)})
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(){
                SpinnerSample(name = stringResource(R.string.taskeditor_status),
                    items = viewModel.getStatus(),
                    selectedItem = viewModel.getStatusName(uiState.taskStatus),
                    {viewModel.updateTaskStatus(it)})
            }
            Row(Modifier
                .fillMaxSize()
                .padding(15.dp,0.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End){
                    var expanded by remember { mutableStateOf(false) }

                    Box {
                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Показать меню") }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.fillMaxWidth(0.5f)) {
                            Text("Удалить", fontSize=18.sp, modifier = Modifier.padding(10.dp).clickable { viewModel.removeTask(navController) })
                        }
                    }
            }
        }
    }
}