package com.coolgirl.majko.Screen.Profile

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.coolgirl.majko.R
import com.coolgirl.majko.components.*
import com.coolgirl.majko.navigation.Screen
import com.coolgirl.majko.ui.theme.MajkoTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@Composable
fun ProfileScreen( navController: NavHostController) {
    val viewModel: ProfileViewModel = koinViewModel()

    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit){
        coroutineScope.launch {
            viewModel.loadData()
        }
    }

    val uiState by viewModel.uiState.collectAsState()

    Column(Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        SetProfileScreen(uiState, {viewModel.updateUserName(it)}, {viewModel.updateUserEmail(it)}, viewModel, navController)
    }

    if (uiState.isChangePassword){
        ChangePassword(uiState,  onUpdateOldPassword = viewModel::updateOldPassword,
            onUpdateNewPassword = viewModel::updateNewPassword,
            onUpdateConfirmPassword = viewModel::updateConfirmPassword,
            onSave = viewModel::changePassword,
            onDismissRequest = viewModel::changePasswordScreen)
    }

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
}

@Composable
fun SetProfileScreen(uiState: ProfileUiState, onUpdateUserName: (String) -> Unit, onUpdateUserEmail: (String) -> Unit, viewModel: ProfileViewModel, navController: NavHostController, launcher: ManagedActivityResultLauncher<String, Uri?> = viewModel.OpenGalery()) {

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.icon_plug),
            contentDescription = "image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clickable { launcher.launch("image/*") }
                .size(200.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = stringResource(R.string.profile_username), fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.width(20.dp))

            LineTextField(uiState.userName, {onUpdateUserName(it)},
                placeholder = stringResource(R.string.login_username), modifier = Modifier.width(150.dp))

            IconButton(onClick = { viewModel.updateNameData()  }) {
                Icon(painter = painterResource(R.drawable.icon_check),
                    contentDescription = "", tint = MaterialTheme.colors.primary)
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = stringResource(R.string.profile_login), fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.width(20.dp))

            LineTextField(uiState.userEmail, {onUpdateUserEmail(it)},
                placeholder = stringResource(R.string.login_login), modifier = Modifier.width(150.dp))

            IconButton(onClick = { viewModel.updateEmailData()  }) {
                Icon(painter = painterResource(R.drawable.icon_check),
                    contentDescription = "", tint = MaterialTheme.colors.primary)
            }
        }

        Row(Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom){
            BlueRoundedButton({ viewModel.changePasswordScreen() }, stringResource(R.string.profile_forget),
                modifier = Modifier.padding(bottom = 10.dp, top = 20.dp))
        }

        Row(Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom) {

            Text(text = stringResource(R.string.profile_logout),
                color = MaterialTheme.colors.primary,
                modifier = Modifier.clickable {
                    viewModel.forgetAccount()
                    navController.navigate(Screen.Login.route){
                        launchSingleTop = true
                        popUpTo(navController.graph.id){
                            inclusive = true
                        }
                    }
                })
        }
    }
}


@Composable
private fun ChangePassword(uiState: ProfileUiState,onUpdateOldPassword: (String) -> Unit,
                           onUpdateNewPassword: (String) -> Unit,
                           onUpdateConfirmPassword: (String) -> Unit,
                           onSave: () -> Unit,
                           onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp)
                .padding(16.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(MaterialTheme.colors.secondary)) {

            WhiteRoundedTextField(uiState.oldPassword, onUpdateOldPassword,
                stringResource(R.string.profile_oldpassword) )
            WhiteRoundedTextField(uiState.newPassword, onUpdateNewPassword,
                stringResource(R.string.profile_newpassword) )
            WhiteRoundedTextField(uiState.confirmPassword, onUpdateConfirmPassword,
                stringResource(R.string.profile_confirmpassword) )


            Row(Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically){
                BlueRoundedButton(onSave, stringResource(R.string.profile_save))
            }
        }
    }
}
