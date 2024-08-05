package com.coolgirl.majko.Screen.Group

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.coolgirl.majko.R
import com.coolgirl.majko.components.*
import kotlinx.coroutines.launch
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

    //экран добавления группы
    if(uiState.isAdding){
        AddGroup(uiState, viewModel::updateGroupName, viewModel::updateGroupDescription,
            viewModel::addGroup, viewModel::addingGroup)
    }

    //экран вступления по инвайту
    if(uiState.isInvite){
        JoinByInviteWindow(uiState, viewModel::updateInvite, viewModel::joinByInvite, viewModel::openInviteWindow)
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
                LongTapPanel( viewModel::removeGroup, uiState, viewModel::updateExpandedLongTap)
            } else {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(65.dp)
                        .padding(all = 10.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .background(color = MaterialTheme.colorScheme.primary),
                    verticalAlignment = Alignment.CenterVertically) {

                    SearchBox(value = uiState.searchString, onValueChange = { viewModel.updateSearchString(it, 2) },
                        placeholder = R.string.group_search)

                    Column {
                        Row {
                            IconButton(onClick = { viewModel.updateExpandedFilter() }, Modifier.size(27.dp)) {
                                Icon(painter = painterResource(R.drawable.icon_filter),
                                    contentDescription = "", tint = MaterialTheme.colorScheme.background)
                            }
                        }
                        FilterDropdown(expanded = uiState.expandedFilter,
                            onDismissRequest = { viewModel.updateExpandedFilter() }, R.string.filter_group_group,
                            { viewModel.updateSearchString(uiState.searchString, it) }, R.string.filter_group_personal, R.string.filter_all)
                    }

                    Spacer(modifier = Modifier.width(5.dp))

                    IconButton(onClick = { viewModel.updateSearchString(uiState.searchString, 2) }, Modifier.size(27.dp)) {
                        Icon(painter = painterResource(R.drawable.icon_filter_off),
                            contentDescription = "", tint = MaterialTheme.colorScheme.background)
                    }

                    Box(Modifier.padding(end = 10.dp)) {
                        IconButton(onClick = { viewModel.updateExpanded()} ) {
                            Icon(painter = painterResource(R.drawable.icon_menu),
                                contentDescription = "", tint = MaterialTheme.colorScheme.background)
                        }
                        DropdownMenu(expanded = uiState.expanded,
                            onDismissRequest = { viewModel.updateExpanded() },
                            modifier = Modifier.fillMaxWidth(0.5f)) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.updateExpanded()
                                        viewModel.openInviteWindow()
                                    }) {
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
                .padding(it)
                .background(MaterialTheme.colorScheme.background)) {
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
                        color = MaterialTheme.colorScheme.onSurface,
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
                        color = MaterialTheme.colorScheme.onSurface,
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
private fun AddGroup(uiState: GroupUiState,  onUpdateName: (String) -> Unit,
                     onUpdateText: (String) -> Unit, onSave: () -> Unit, onDismissRequest: () -> Unit){
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp)
                .padding(16.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(MaterialTheme.colorScheme.secondary)) {

            WhiteRoundedTextField(uiState.newGroupName, onUpdateName,
                stringResource(R.string.group_name) )

            WhiteRoundedTextField(uiState.newGroupDescription, onUpdateText,
                stringResource(R.string.group_description),
                Modifier
                    .fillMaxHeight(0.75f)
                    .padding(bottom = 20.dp))

            Row(Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center){
                BlueRoundedButton( onSave, stringResource(R.string.project_add))
            }
        }
    }
}

@Composable
private fun JoinByInviteWindow(uiState: GroupUiState, onUpdateInvite: (String)->Unit, onJoinByInvite: ()-> Unit,
                              onDismissRequest: () -> Unit){
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(MaterialTheme.colorScheme.secondary)
        ) {

            WhiteRoundedTextField(uiState.invite, onUpdateInvite,
                stringResource(R.string.invite), Modifier.padding(bottom = 20.dp))

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(all = 10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically){
                if(uiState.inviteMessage.equals("")){
                    BlueRoundedButton(onJoinByInvite, stringResource(R.string.project_joininvite))

                }else {
                    Text(text = uiState.inviteMessage, color = MaterialTheme.colorScheme.background)
                    BlueRoundedButton(onDismissRequest, stringResource(R.string.projectedit_close))
                }
            }
        }
    }
}

@Composable
fun LongTapPanel(onRemoving: ()-> Unit, uiState: GroupUiState, onUpdateExpandedLongTap:()->Unit){
    Row(
        Modifier
            .fillMaxWidth()
            .height(65.dp)
            .background(color = MaterialTheme.colorScheme.secondary),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End){


        Box(Modifier.padding(all = 10.dp)) {
            IconButton(onClick = onUpdateExpandedLongTap ) {
                Icon(painter = painterResource(R.drawable.icon_menu),
                    contentDescription = "", tint = MaterialTheme.colorScheme.background)
            }
            DropdownMenu(
                expanded = uiState.expandedLongTap,
                onDismissRequest = onUpdateExpandedLongTap,
                modifier = Modifier.fillMaxWidth(0.5f)) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable { onRemoving()
                            onUpdateExpandedLongTap()
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