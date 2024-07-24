package com.coolgirl.majko.Sc

import com.coolgirl.majko.Screen.GroupEditor.GroupEditorUiState
import com.coolgirl.majko.Screen.GroupEditor.GroupEditorViewModel
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.coolgirl.majko.R
import com.coolgirl.majko.components.ProjectCard
import com.coolgirl.majko.data.dataStore.UserDataStore

@Composable
fun GroupEditorScreen(navController: NavHostController, group_id: String){
    val cont = LocalContext.current
    val viewModel : GroupEditorViewModel = remember{ GroupEditorViewModel(UserDataStore(cont), group_id) }
    val uiState by viewModel.uiState.collectAsState()
    SetGroupEditorScreen(uiState, viewModel, navController)
}

@Composable
fun SetGroupEditorScreen(uiState: GroupEditorUiState, viewModel: GroupEditorViewModel, navController: NavHostController){
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
                modifier = Modifier.clickable { viewModel.saveGroup(navController) },
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
                            .clickable { viewModel.removeGroup(navController) })
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
            uiState.groupData?.let {
                BasicTextField(
                    value = it.title,
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 15.dp),
                    textStyle = TextStyle.Default.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                    onValueChange = { viewModel.updateGroupName(it) },
                    maxLines = 2,
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
                if(uiState.groupData.projects_group!=null){
                    val projectData = uiState.groupData.projects_group

                    for (item in projectData){
                        ProjectCard(navController, projectData = item, onLongTap = {})
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
        if(uiState.is_adding){
            LazyRow(
                Modifier
                    .fillMaxWidth()
                    .padding(all = 5.dp)) {
                if(uiState.projectData!=null){
                    val projectData = uiState.projectData
                    val count = uiState.projectData.size?:0
                    items(count) { rowIndex ->
                        Column(Modifier.width(200.dp)) {
                            ProjectCard(navController, projectData = projectData[rowIndex], onLongTap = {})
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

        //мемберы
        if(!uiState.groupData?.members.isNullOrEmpty()){
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

    if(uiState.is_invite){
        SetInviteWindow(uiState, viewModel, { viewModel.newInvite()})
    }
}

@Composable
fun SetInviteWindow(uiState: GroupEditorUiState, viewModel : GroupEditorViewModel, onDismissRequest: () -> Unit){
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

