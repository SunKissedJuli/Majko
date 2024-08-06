package com.coolgirl.majko.Screen.TaskEditor

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.coolgirl.majko.components.*
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun TaskEditorScreen(navController: NavHostController, taskId : String){
    val viewModel: TaskEditorViewModel = koinViewModel()

    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit){
        coroutineScope.launch {
            viewModel.loadData(taskId)
        }
    }

    val uiState by viewModel.uiState.collectAsState()

    BackHandler { viewModel.updateExitDialog() }

    if (uiState.exitDialog) {
        ExitAlertDialog(
            onConfirm = { viewModel.updateExitDialog()
                viewModel.saveTask(navController) },
            onDismiss = { viewModel.updateExitDialog()
                navController.popBackStack()})
    }

    SetTaskEditorScreen(uiState, {viewModel.updateTaskText(it)},
        { viewModel.updateTaskName(it) }, viewModel, navController)
}

@Composable
fun SetTaskEditorScreen(uiState: TaskEditorUiState, onUpdateTaskText: (String) -> Unit, onUpdateTaskName:(String) -> Unit, viewModel: TaskEditorViewModel, navController: NavHostController) {
    //добавление субтаска
    if(uiState.isAdding){
        AddNewTask(uiState, viewModel, {viewModel.addingTask()})
    }

    Scaffold(
        topBar = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                ButtonBack({viewModel.saveTask(navController)})

                Box {
                    IconButton(onClick = { viewModel.updateExpanded() }) {
                        Icon(
                            painter = painterResource(R.drawable.icon_menu),
                            contentDescription = "", tint = MaterialTheme.colorScheme.background
                        )
                    }
                    DropdownMenu(
                        expanded = uiState.expanded,
                        onDismissRequest = { viewModel.updateExpanded() },
                        modifier = Modifier.fillMaxWidth(0.5f)) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.removeTask(navController)
                                    viewModel.updateExpanded()
                                }){
                            Text(stringResource(R.string.project_delite), fontSize=18.sp, color = MaterialTheme.colorScheme.onSecondary,
                                modifier = Modifier.padding(all = 10.dp))
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
                .background(colorResource(uiState.backgroundColor))
                .padding(it)) {

            Column(
                Modifier
                    .fillMaxWidth()) {
                BasicTextField(
                    value = uiState.taskName,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 15.dp),
                    textStyle = TextStyle.Default.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                    onValueChange = onUpdateTaskName,
                    decorationBox = { innerTextField ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            if (uiState.taskName.isEmpty()) {
                                Text(text = stringResource(R.string.taskeditor_name),
                                    color = MaterialTheme.colorScheme.onSurface,fontSize = 20.sp) }
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
                                    color = MaterialTheme.colorScheme.onSurface,fontSize = 18.sp) }
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

                            Icon(painter = painterResource(R.drawable.icon_plus),
                                contentDescription = "", tint = MaterialTheme.colorScheme.background)

                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = stringResource(R.string.taskeditor_add),
                                fontSize = 18.sp, fontWeight = FontWeight.Medium,)
                        }
                        HorizontalLine()
                    }

                    //добавление note
                    if(uiState.newNote){
                        Column{
                            BasicTextField(
                                value = uiState.noteText,
                                modifier = Modifier
                                    .padding(horizontal = 20.dp, vertical = 15.dp),
                                textStyle = TextStyle.Default.copy(fontSize = 18.sp),
                                onValueChange = {viewModel.updateNoteText(it)},
                                decorationBox = { innerTextField ->
                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        if (uiState.noteText.isEmpty()) {
                                            Text(text = stringResource(R.string.taskeditor_hint),
                                                color = MaterialTheme.colorScheme.onSurface,fontSize = 18.sp) }
                                        innerTextField() } })

                            Row(Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center){

                                Button(onClick = { viewModel.addNote() },
                                    shape = CircleShape,
                                    modifier = Modifier
                                        .fillMaxWidth(0.65f)
                                        .padding(vertical = 10.dp),
                                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)) {
                                    Text(text = stringResource(R.string.project_add),
                                        color = MaterialTheme.colorScheme.background,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Medium)
                                }
                            }
                        }
                    }

                    //отображение notes
                    if(!uiState.notes.isNullOrEmpty()){
                        SetNotes(uiState,  onUpdateNoteText = viewModel::updateOldNoteText,
                            onSaveNote = viewModel::saveUpdateNote,
                            onRemoveNote = viewModel::removeNote)
                    }

                    //отображение субтасков
                    if(!uiState.subtask.isNullOrEmpty()){
                        LazyRow(Modifier.padding(all = 5.dp)) {
                            val subtask = uiState.subtask
                            val count = uiState.subtask?.size?:0
                            items(count) { rowIndex ->
                                Column(Modifier.width(200.dp)) {
                                    TaskCard(navController,  colorResource(viewModel.getPriority(subtask[rowIndex].priority)),
                                        viewModel.getStatusName(subtask[rowIndex].status)?: stringResource(R.string.common_no),
                                        subtask[rowIndex])
                                }
                            }
                        }
                    }
                }

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                    BlueRoundedButton({ viewModel.addingTask() }, stringResource(R.string.taskeditor_addtask),
                        modifier = Modifier.padding(bottom = 10.dp, top = 20.dp))
                }
            }

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, top = 5.dp, end = 15.dp, bottom = 15.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(color = MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top) {

                Column(Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {

                    DeadlineDatePicker(currentDeadline = uiState.taskDeadline,
                        onUpdateDeadline = { newDate -> viewModel.updateTaskDeadlie(newDate) })

                    HorizontalLine()
                    if(uiState.taskPriorityName.isNotEmpty()||uiState.taskId=="0"){
                        SpinnerSample(name = stringResource(R.string.taskeditor_priority),
                            items = viewModel.getPriority(),
                            selectedItem = uiState.taskPriorityName,
                            {viewModel.updateTaskPriority(it)})
                    }

                    HorizontalLine()
                    Text(text = stringResource(R.string.taskeditor_project) + " " +
                            (uiState.taskProjectObj.name.ifEmpty { stringResource(R.string.common_no) }),
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSecondary)
                    HorizontalLine()
                    if(uiState.taskStatusName.isNotEmpty()||uiState.taskId=="0"){
                        SpinnerSample(
                            name = stringResource(R.string.taskeditor_status),
                            items = viewModel.getStatus(),
                            selectedItem = uiState.taskStatusName,
                            {viewModel.updateTaskStatus(it) }
                        )
                    }
                    HorizontalLine()
                }
            }
        }
    }


}

@Composable
private fun AddNewTask(uiState: TaskEditorUiState, viewModel: TaskEditorViewModel, onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(MaterialTheme.colorScheme.secondary)) {
            Column {
                Column(
                    Modifier
                        .padding(all = 15.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(color = MaterialTheme.colorScheme.background),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top) {

                    BasicTextField(
                        value = uiState.subtaskName,
                        modifier = Modifier
                            .padding(start = 18.dp, top = 15.dp, bottom = 5.dp)
                            .fillMaxHeight(0.09f),
                        textStyle = TextStyle.Default.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        onValueChange = { viewModel.updateSubtaskName(it) },
                        maxLines = 2,
                        decorationBox = { innerTextField ->
                            Row(modifier = Modifier.fillMaxWidth()) {
                                if (uiState.subtaskName.isEmpty()) {
                                    Text(text = stringResource(R.string.taskeditor_name), color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 20.sp, maxLines = 2)
                                }
                                innerTextField()
                            }
                        })
                    BasicTextField(
                        value = uiState.subtaskText,
                        modifier = Modifier
                            .fillMaxHeight(0.25f)
                            .padding(horizontal = 18.dp),
                        textStyle = TextStyle.Default.copy(fontSize = 18.sp),
                        onValueChange = { viewModel.updateSubtaskText(it) },
                        decorationBox = { innerTextField ->
                            Row(modifier = Modifier.fillMaxWidth()) {
                                if (uiState.subtaskText.isEmpty()) {
                                    Text(text = stringResource(R.string.taskeditor_hint),
                                        color = MaterialTheme.colorScheme.onSurface, fontSize = 18.sp, maxLines = 4)
                                }
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
                        .background(color = MaterialTheme.colorScheme.background),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top) {

                    Column(Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {

                        DeadlineDatePicker(
                            currentDeadline = uiState.subtaskDeadline,
                            onUpdateDeadline = { newDate -> viewModel.updateSubtaskDeadlie(newDate) })
                        HorizontalLine()

                            SpinnerSample(name = stringResource(R.string.taskeditor_priority),
                                items = viewModel.getPriority(),
                                selectedItem = viewModel.getPriorityName(uiState.subtaskPriority),
                                { viewModel.updateSubtaskPriority(it) })
                        HorizontalLine()
                        Text(text = stringResource(R.string.taskeditor_project) + " " +
                                    (uiState.taskProjectObj.name.ifEmpty { stringResource(R.string.common_no) }),
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                        HorizontalLine()

                        SpinnerSample(
                            name = stringResource(R.string.taskeditor_status),
                            items = viewModel.getStatus(),
                            selectedItem = viewModel.getStatusName(uiState.subtaskStatus),
                            { viewModel.updateSubtaskStatus(it) })
                        HorizontalLine()
                    }
                }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.Center) {
                    Button(onClick = { viewModel.saveSubtask() },
                        shape = CircleShape,
                        modifier = Modifier
                            .fillMaxWidth(0.65f)
                            .padding(vertical = 10.dp),
                        colors = ButtonDefaults.buttonColors(
                            MaterialTheme.colorScheme.primary)) {
                        Text(text = stringResource(R.string.project_add), color = MaterialTheme.colorScheme.background,
                            fontSize = 18.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

@Composable
private fun SetNotes(uiState: TaskEditorUiState, onUpdateNoteText: (String, String) -> Unit,
                     onSaveNote: (String, String) -> Unit, onRemoveNote: (String) -> Unit){
    Column(Modifier.padding(start = 20.dp, top = 10.dp, end = 10.dp, bottom = 10.dp)){
        for(item in uiState.notes!!){
            Row(Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically) {

                Icon(painter = painterResource(R.drawable.icon_line),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.background)
                Spacer(modifier = Modifier.width(10.dp))

                BasicTextField(
                    value = item.note,
                    textStyle = TextStyle.Default.copy(fontSize = 18.sp),
                    onValueChange = {onUpdateNoteText(it, item.id)},
                    modifier = Modifier.fillMaxWidth(0.7f),
                    decorationBox = { innerTextField ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            if (item.note.isEmpty()) {
                                Text(text = stringResource(R.string.taskeditor_hint),
                                    color = MaterialTheme.colorScheme.surface,fontSize = 18.sp) }
                            innerTextField() } })
                Spacer(modifier = Modifier.width(5.dp))

                IconButton(onClick = { onSaveNote(item.id, item.note) }) {
                    Icon(painter = painterResource(R.drawable.icon_check),
                        contentDescription = "", tint = MaterialTheme.colorScheme.primary)
                }

                IconButton(onClick = { onRemoveNote(item.id) }) {
                    Icon(painter = painterResource(R.drawable.icon_delete),
                        contentDescription = "", tint = MaterialTheme.colorScheme.onSecondary)
                }
            }
        }
    }
}
