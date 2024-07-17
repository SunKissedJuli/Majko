package com.coolgirl.majko.Screen.TaskEditor

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.coolgirl.majko.R
import com.coolgirl.majko.commons.HorizontalLine
import com.coolgirl.majko.commons.SpinnerItems
import com.coolgirl.majko.commons.SpinnerSample
import com.coolgirl.majko.commons.TaskCard
import com.coolgirl.majko.data.dataStore.UserDataStore
import okhttp3.internal.wait
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
            .verticalScroll(rememberScrollState())
            .background(colorResource(uiState.backgroundColor))) {
        Row(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.08f)
                .background(colorResource(R.color.blue))
                .padding(10.dp, 0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(modifier = Modifier.clickable { viewModel.saveTask(navController) }, text = "←", fontWeight = FontWeight.Medium,
                color = colorResource(R.color.white), fontSize = 50.sp,
            )

            var expanded by remember { mutableStateOf(false) }

            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Показать меню", tint = colorResource(R.color.white))
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth(0.5f)) {
                    Text("Удалить", fontSize=18.sp, modifier = Modifier
                        .padding(10.dp)
                        .clickable { viewModel.removeTask(navController) })
                }
            }
            //   Text(text = noteData, fontSize = 18.sp)
        }
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f)) {
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
        Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {
            Column(Modifier.padding(20.dp, 20.dp, 20.dp, 0.dp)) {
                Row(
                    Modifier
                        .fillMaxHeight()
                        .clickable { viewModel.addNewNote() },
                    verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "+",
                        fontSize = 55.sp,
                        fontWeight = FontWeight.Medium,
                        color = colorResource(R.color.white)
                    )
                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "Добавить запись",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }
                HorizontalLine()
            }


            //добавление note
            if(uiState.newNote){
                Column(){
                    BasicTextField(
                        value = uiState.noteText,
                        modifier = Modifier
                            .padding(20.dp, 15.dp)
                            .fillMaxHeight(0.09f),
                        textStyle = TextStyle.Default.copy(fontSize = 18.sp),
                        onValueChange = {viewModel.updateNoteText(it)},
                        maxLines = 2,
                        decorationBox = { innerTextField ->
                            Row(modifier = Modifier.fillMaxWidth()) {
                                if (uiState.noteText.isEmpty()) {
                                    Text(text = stringResource(R.string.taskeditor_hint),
                                        color = colorResource(R.color.gray),fontSize = 18.sp) }
                                innerTextField() } })

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center){

                        Button(
                            onClick = { viewModel.addNote() },
                            shape = CircleShape,
                            modifier = Modifier
                                .fillMaxWidth(0.65f)
                                .padding(0.dp, 10.dp),
                            colors = ButtonDefaults.buttonColors(colorResource(R.color.blue))) {
                            Text(
                                text = stringResource(R.string.project_add),
                                color = colorResource(R.color.white),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                }
            }

            //отображение notes
            if(uiState.notes!=null){
                Column(Modifier.padding(20.dp,10.dp,10.dp,10.dp)){
                    for(item in uiState.notes!!){
                        Row(Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "-",
                                fontSize = 55.sp,
                                fontWeight = FontWeight.Medium,
                                color = colorResource(R.color.white)
                            )
                            Spacer(modifier = Modifier.width(10.dp))

                            BasicTextField(
                                value = item.note,
                                textStyle = TextStyle.Default.copy(fontSize = 18.sp),
                                onValueChange = {viewModel.updateOldNoteText(it, item.id)},
                                modifier = Modifier.fillMaxWidth(0.7f),
                                decorationBox = { innerTextField ->
                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        if (item.note.isEmpty()) {
                                            Text(text = stringResource(R.string.taskeditor_hint),
                                                color = colorResource(R.color.gray),fontSize = 18.sp) }
                                        innerTextField() } })

                            Spacer(modifier = Modifier.width(5.dp))
                            IconButton(onClick = { viewModel.saveUpdateNote(item.id, item.note) }) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = "Сохранить",
                                    tint = colorResource(R.color.blue)
                                )
                            }

                            IconButton(onClick = {  viewModel.removeNote(item.id) }) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Удалить",
                                    tint = colorResource(R.color.black)
                                )
                            }
                        }
                    }
                }
            }

            //отображение субтасков
            if(uiState.subtask!=null){
                LazyRow(Modifier.padding(5.dp)) {
                    val subtask = uiState.subtask
                    val count = uiState.subtask?.size?:0
                    items(count) { rowIndex ->
                        TaskCard(navController, viewModel.getPriority(subtask[rowIndex].priority),
                            viewModel.getStatus(subtask[rowIndex].status), subtask[rowIndex], {}, {})
                    }
                }
            }

        }

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(15.dp, 5.dp, 15.dp, 15.dp)
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

                Column(Modifier.padding(20.dp,10.dp)) {

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
                    SpinnerSample(name = stringResource(R.string.taskeditor_project), items = listOf(
                        SpinnerItems("1", "Item 1"),
                        SpinnerItems("2", "Item 2"),
                        SpinnerItems("3", "Item 3")),
                        selectedItem = "",
                        {viewModel.updateTaskProject(it)})
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


        }
    }
