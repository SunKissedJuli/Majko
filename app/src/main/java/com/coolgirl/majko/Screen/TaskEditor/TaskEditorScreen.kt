package com.coolgirl.majko.Screen.TaskEditor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.coolgirl.majko.R
import com.coolgirl.majko.data.dataStore.UserDataStore

@Composable
fun TaskEditorScreen(navController: NavHostController, task_id : Int){
    val viewModel : TaskEditorViewModel = TaskEditorViewModel(UserDataStore(LocalContext.current))
    val uiState by viewModel.uiState.collectAsState()

    SetTaskEditorScreen(uiState, {viewModel.updateTaskText(it)}, {viewModel.saveTask(navController)})

}

@Composable
fun SetTaskEditorScreen(uiState: TaskEditorUiState, onUpdateTaskText: (String) -> Unit, onSaveTask: () -> Unit){
        Column(
            Modifier
                .fillMaxSize()
                .background(colorResource(R.color.white))) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.1f)
                    .background(colorResource(R.color.blue)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround) {
                Text(text = "←", fontWeight = FontWeight.Black,
                    color = colorResource(R.color.white), fontSize = 50.sp,
                    modifier = Modifier.clickable { onSaveTask })
                //   Text(text = noteData, fontSize = 18.sp)
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)) {
                BasicTextField(
                    value = uiState.taskText,
                    onValueChange = onUpdateTaskText,
                    modifier = Modifier.padding(20.dp),
                    textStyle = TextStyle.Default.copy(fontSize = 18.sp),
                    decorationBox = { innerTextField ->
                        Box {
                            if (uiState.taskText.isEmpty()) {
                                Text(
                                    text = "Начните писать..",
                                    color = colorResource(R.color.gray),
                                    fontSize = 18.sp
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
        }
}
