package com.coolgirl.majko.Screen.TaskEditor

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.ButtonDefaults
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
import androidx.navigation.NavHostController
import com.coolgirl.majko.R
import com.coolgirl.majko.components.HorizontalLine
import com.coolgirl.majko.components.SpinnerSample
import com.coolgirl.majko.components.TaskCard
import com.coolgirl.majko.data.dataStore.UserDataStore
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
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(modifier = Modifier.clickable { viewModel.saveTask(navController) }, text = stringResource(R.string.common_back), fontWeight = FontWeight.Medium,
                color = colorResource(R.color.white), fontSize = 50.sp,
            )

            var expanded by remember { mutableStateOf(false) }

            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "", tint = colorResource(R.color.white))
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth(0.5f)) {
                    Text(stringResource(R.string.project_delite), fontSize=18.sp, modifier = Modifier
                        .padding(all = 10.dp)
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
                    .padding(horizontal = 20.dp, vertical = 15.dp)
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
                modifier = Modifier.padding(horizontal = 20.dp),
                textStyle = TextStyle.Default.copy(fontSize = 18.sp),
                onValueChange = onUpdateTaskText,
                decorationBox = { innerTextField ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        if (uiState.taskText.isEmpty()) {
                            Text(text = stringResource(R.string.taskeditor_hint),
                                color = colorResource(R.color.gray),fontSize = 18.sp) }
                        innerTextField() } })
        }


        //отображается только при редактировании, при добавлении таски нельзя добавить субтаску или задачу
            if(!uiState.taskId.equals("0")){
                Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {
                    Column(Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp)) {
                        Row(
                            Modifier
                                .fillMaxHeight()
                                .clickable { viewModel.addNewNote() },
                            verticalAlignment = Alignment.CenterVertically) {

                            Image(painter = painterResource(R.drawable.icon_plus),
                                contentDescription = "")

                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = stringResource(R.string.taskeditor_add),
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
                                .padding(horizontal = 20.dp, vertical = 15.dp)
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
                                    .padding(vertical = 10.dp),
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
                    Column(Modifier.padding(start = 20.dp, top = 10.dp, end = 10.dp, bottom = 10.dp)){
                        for(item in uiState.notes!!){
                            Row(Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = stringResource(R.string.common_dash),
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
                                    Image(painter = painterResource(R.drawable.icon_check),
                                        contentDescription = "")
                                }

                                IconButton(onClick = {  viewModel.removeNote(item.id) }) {
                                    Image(painter = painterResource(R.drawable.icon_delete),
                                        contentDescription = "")
                                }
                            }
                        }
                    }
                } //отображение субтасков
                if(uiState.subtask!=null){
                    LazyRow(Modifier.padding(all = 5.dp)) {
                        val subtask = uiState.subtask
                        val count = uiState.subtask?.size?:0
                        items(count) { rowIndex ->
                            Column(Modifier.width(200.dp)) {
                                TaskCard(navController, viewModel.getPriority(subtask[rowIndex].priority),
                                    viewModel.getStatus(subtask[rowIndex].status), subtask[rowIndex], {}, {})
                            }
                        }
                    }
                }
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                Button(
                    onClick = { viewModel.addingTask() },
                    shape = CircleShape,
                    modifier = Modifier
                        .fillMaxWidth(0.65f)
                        .padding(vertical = 10.dp),
                    colors = ButtonDefaults.buttonColors(colorResource(R.color.blue))
                ) {
                    Text(
                        text = stringResource(R.string.taskeditor_addtask),
                        color = colorResource(R.color.white),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            //добавление субтаска
            if(uiState.isAdding){
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
                            value = uiState.subtaskName,
                            modifier = Modifier
                                .padding(horizontal = 20.dp, vertical = 15.dp)
                                .fillMaxHeight(0.09f),
                            textStyle = TextStyle.Default.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                            onValueChange = {viewModel.updateSubtaskName(it)},
                            maxLines = 2,
                            decorationBox = { innerTextField ->
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    if (uiState.subtaskName.isEmpty()) {
                                        Text(text = stringResource(R.string.taskeditor_name),
                                            color = colorResource(R.color.gray),fontSize = 20.sp) }
                                    innerTextField() } })
                        BasicTextField(
                            value = uiState.subtaskText,
                            modifier = Modifier.padding(horizontal = 20.dp),
                            textStyle = TextStyle.Default.copy(fontSize = 18.sp),
                            onValueChange = {viewModel.updateSubtaskText(it)},
                            decorationBox = { innerTextField ->
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    if (uiState.subtaskText.isEmpty()) {
                                        Text(text = stringResource(R.string.taskeditor_hint),
                                            color = colorResource(R.color.gray),fontSize = 18.sp) }
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
                                viewModel.updateSubtaskDeadlie(formattedData)
                            }, mYear, mMonth, mDay)

                        Column(Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {

                            var formattedData : String = ""
                            if(uiState.subtaskDeadline!="") {
                                val dateTime = LocalDateTime.parse(uiState.subtaskDeadline, DateTimeFormatter.ofPattern("yyyy-M-d H:m:s"))
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
                                selectedItem = viewModel.getPriorityName(uiState.subtaskPriority),
                                {viewModel.updateSubtaskPriority(it)}
                            )
                            HorizontalLine()
                            Text(text= stringResource(R.string.taskeditor_project) + (" ") + (uiState.taskProjectObj?.name ?: stringResource(R.string.common_no)), fontSize = 18.sp)
                            HorizontalLine()
                            SpinnerSample(
                                name = stringResource(R.string.taskeditor_status),
                                items = viewModel.getStatus(),
                                selectedItem = viewModel.getStatusName(uiState.taskStatus),
                                {viewModel.updateSubtaskStatus(it) }
                            )

                            HorizontalLine()
                        }
                    }

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 30.dp),
                        horizontalArrangement = Arrangement.Center){
                        Button(
                            onClick = { viewModel.saveSubtask() },
                            shape = CircleShape,
                            modifier = Modifier
                                .fillMaxWidth(0.65f)
                                .padding(vertical = 10.dp),
                            colors = ButtonDefaults.buttonColors(
                                colorResource(R.color.blue))) {
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
                Text(text= stringResource(R.string.taskeditor_project) + (" ") + (uiState.taskProjectObj?.name ?: stringResource(R.string.common_no)), fontSize = 18.sp)
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
