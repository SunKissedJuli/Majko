package com.coolgirl.majko.Screen.Group

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.coolgirl.majko.R
import com.coolgirl.majko.components.*
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun GroupScreen(navController: NavHostController) {
    val viewModel: GroupViewModel = koinViewModel()

    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit){
        coroutineScope.launch {
            viewModel.loadData()
        }
    }

    val uiState by viewModel.uiState.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var expandedFilter by remember { mutableStateOf(false) }

    //экран добавления группы
    if(uiState.isAdding){
        AddGroup(uiState, viewModel, { viewModel.addingGroup()})
    }

    //экран вступления по инвайту
    if(uiState.isInvite){
        JoinByInviteWindow(uiState, viewModel, { viewModel.openInviteWindow()})
    }

    //снекбары
    Box(Modifier.fillMaxSize()) {
        if(uiState.isError){
            Row(Modifier.align(Alignment.BottomCenter)) {
                uiState.errorMessage?.let { ErrorSnackbar(it, { viewModel.isError(null) }) }
            }
        }
        if(uiState.isMessage){
            Row(Modifier.align(Alignment.BottomCenter)) {
                uiState.message?.let { MessageSnackbar(it, { viewModel.isMessage(null) }) }
            }
        }
    }

    Scaffold(
        topBar = {

            //панель при длинном нажатии
            if (uiState.isLongtap) {
                LongTapPanel({ viewModel.removeGroup() })
            } else {
                Row(Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.1f)
                        .padding(all = 10.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .background(color = MaterialTheme.colors.primary),
                    verticalAlignment = Alignment.CenterVertically) {

                    SearchBox(value = uiState.searchString, onValueChange = { viewModel.updateSearchString(it, 2) },
                        placeholder = R.string.group_search)

                    Column {
                        Row {
                            Icon(painter = painterResource(R.drawable.icon_filter),
                                modifier = Modifier.clickable { expandedFilter = !expandedFilter },
                                contentDescription = "", tint = MaterialTheme.colors.surface)
                        }
                        FilterDropdown(expanded = expandedFilter,
                            onDismissRequest = { expandedFilter = it }, R.string.filter_group_group,
                            { viewModel.updateSearchString(uiState.searchString, it) }, R.string.filter_group_personal, R.string.filter_all)
                    }

                    Spacer(modifier = Modifier.width(5.dp))
                    Icon(painter = painterResource(R.drawable.icon_filter_off),
                        modifier = Modifier.clickable { viewModel.updateSearchString(uiState.searchString, 2) },
                        contentDescription = "", tint = MaterialTheme.colors.surface)

                    Box(Modifier.padding(end = 10.dp)) {
                        IconButton(onClick = { expanded = true }) {
                            Image(painter = painterResource(R.drawable.icon_menu), contentDescription = "")
                        }
                        DropdownMenu(expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.fillMaxWidth(0.5f)) {
                            Row(Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.openInviteWindow() }) {
                                Text(stringResource(R.string.project_joininvite), fontSize = 18.sp, modifier = Modifier.padding(all = 10.dp))
                            }
                        }
                    }
                }
            }
        }
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(it)) {
            Column(Modifier.fillMaxSize()) {
                SetGroupScreen(uiState, navController, viewModel)
            }

            Box(Modifier.align(Alignment.BottomEnd)){
                AddButton(onClick = { viewModel.addingGroup() }, id = "")
            }
        }
    }
}

@Composable
fun SetGroupScreen(uiState: GroupUiState, navController: NavHostController, viewModel: GroupViewModel) {
    val personalGroup = uiState.searchPersonalGroup
    val groupGroup = uiState.searchGroupGroup

    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (!groupGroup.isNullOrEmpty()) {
                item {
                    Text(text = stringResource(R.string.group_group),
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier.padding(start = 15.dp, top = 10.dp, bottom = 10.dp))
                }
                items(groupGroup) { group ->
                    GroupCard(navController, groupData = group,
                        onLongTap = { viewModel.openPanel(it) },
                        onLongTapRelease = { viewModel.openPanel(it) },
                        isSelected = uiState.longtapGroupId.contains(group.id))
                }
            }

            if (!personalGroup.isNullOrEmpty()) {
                item {
                    Text(text = stringResource(R.string.group_personal),
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier.padding(start = 15.dp, top = 10.dp, bottom = 10.dp))
                }
                items(personalGroup) { group ->
                    GroupCard(navController, groupData = group,
                        onLongTap = { viewModel.openPanel(it) },
                        onLongTapRelease = { viewModel.openPanel(it) },
                        isSelected = uiState.longtapGroupId.contains(group.id))
                }
            }
        }
    }
}

@Composable
private fun AddGroup(uiState: GroupUiState, viewModel: GroupViewModel, onDismissRequest: () -> Unit){
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp)
                .padding(16.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(MaterialTheme.colors.secondary)) {

            OutlinedTextField(
                value = uiState.newGroupName,
                onValueChange = {viewModel.updateGroupName(it)},
                Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp),
                shape = RoundedCornerShape(30.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colors.background, unfocusedContainerColor = MaterialTheme.colors.background,
                    focusedBorderColor = MaterialTheme.colors.background, unfocusedBorderColor = MaterialTheme.colors.background),
                placeholder = {Text(text = stringResource(R.string.group_name), color = MaterialTheme.colors.onSurface)})

            OutlinedTextField(value = uiState.newGroupDescription,
                onValueChange = {viewModel.updateGroupDescription(it)},
                Modifier
                    .fillMaxHeight(0.75f)
                    .padding(start = 20.dp, top = 20.dp, bottom = 20.dp, end = 20.dp),
                shape = RoundedCornerShape(30.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colors.background, unfocusedContainerColor = MaterialTheme.colors.background,
                    focusedBorderColor = MaterialTheme.colors.background, unfocusedBorderColor = MaterialTheme.colors.background),
                placeholder = {Text(text = stringResource(R.string.group_description), color = MaterialTheme.colors.onSurface)})

            Row(Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center){
                Button(onClick = { viewModel.addGroup()},
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .border(3.dp, colorResource(R.color.white), RoundedCornerShape(30.dp)),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)) {
                    Text(text = stringResource(R.string.project_add), color = MaterialTheme.colors.background,
                        fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun JoinByInviteWindow(uiState: GroupUiState, viewModel: GroupViewModel, onDismissRequest: () -> Unit){
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(MaterialTheme.colors.secondary)
        ) {

            OutlinedTextField(
                value = uiState.invite,
                onValueChange = { viewModel.updateInvite(it) },
                Modifier.padding(all = 20.dp),
                shape = RoundedCornerShape(30.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colors.background,
                    unfocusedContainerColor = MaterialTheme.colors.background,
                    focusedBorderColor = MaterialTheme.colors.background,
                    unfocusedBorderColor = MaterialTheme.colors.background
                ),
            )

            if(uiState.inviteMessage.equals("")){
                Button(onClick = { viewModel.joinByInvite() },
                    shape = CircleShape,
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .padding(vertical = 10.dp)
                        .align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)) {
                    Text(text = stringResource(R.string.project_joininvite), color = MaterialTheme.colors.background,
                        fontSize = 18.sp, fontWeight = FontWeight.Medium)
                }
            }else {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(all = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically){
                    Text(text = uiState.inviteMessage, color = MaterialTheme.colors.background)
                }

                Button(onClick = { viewModel.openInviteWindow() },
                    shape = CircleShape,
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .padding(vertical = 10.dp)
                        .align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)) {
                    Text(text = stringResource(R.string.projectedit_close), color = MaterialTheme.colors.background,
                        fontSize = 18.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
fun LongTapPanel(onRemoving: ()-> Unit){
    var expandedLongPanel by remember { mutableStateOf(false) }
    Row(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.1f)
            .background(color = MaterialTheme.colors.secondary),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End){


        Box(Modifier.padding(all = 10.dp)) {
            IconButton(onClick = { expandedLongPanel = true }) {
                Image(painter = painterResource(R.drawable.icon_menu),
                    contentDescription = "")
            }
            DropdownMenu(
                expanded = expandedLongPanel,
                onDismissRequest = { expandedLongPanel = false },
                modifier = Modifier.fillMaxWidth(0.5f)) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            onRemoving
                            expandedLongPanel = false
                        }) {
                    Text(
                        stringResource(R.string.project_delite),
                        fontSize = 18.sp,
                        modifier = Modifier.padding(all = 10.dp)
                    )
                }
            }
        }
    }
}