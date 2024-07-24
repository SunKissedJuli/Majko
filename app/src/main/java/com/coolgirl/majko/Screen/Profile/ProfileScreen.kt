package com.coolgirl.majko.Screen.Profile

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.coolgirl.majko.R
import com.coolgirl.majko.navigation.Screen
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel


@Composable
fun ProfileScreen( navController: NavHostController) {
    val viewModel = getViewModel<ProfileViewModel>()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit){
        coroutineScope.launch {
            viewModel.loadData()
        }
    }

    val uiState by viewModel.uiState.collectAsState()
    Column(
        Modifier
            .fillMaxSize()
            .alpha(uiState.isAddingBackground)
            .background(MaterialTheme.colors.background)) {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.93f)) {
            SetProfileScreen(uiState, {viewModel.updateUserName(it)}, {viewModel.updateUserEmail(it)}, viewModel, navController)
        }
    }

    if (uiState.isAdding){
        ChangePassword(uiState, viewModel, {viewModel.changePasswordScreen()})
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
            TextField(
                value = uiState.userName,
                modifier = Modifier
                    .height(55.dp)
                    .width(150.dp),
                onValueChange = onUpdateUserName,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colors.background,
                    unfocusedContainerColor = MaterialTheme.colors.background
                )
            )
            IconButton(onClick = { viewModel.updateNameData()  }) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "",
                    tint = MaterialTheme.colors.primary
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = stringResource(R.string.profile_login), fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.width(20.dp))
            TextField(
                value = uiState.userEmail,
                modifier = Modifier
                    .height(55.dp)
                    .width(150.dp),
                onValueChange = onUpdateUserEmail,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colors.background,
                    unfocusedContainerColor = MaterialTheme.colors.background
                )
            )

            IconButton(onClick = { viewModel.updateEmailData() }) {
                Image(painter = painterResource(R.drawable.icon_check), contentDescription = "")
            }
        }

        Row(
            Modifier
                .fillMaxHeight(0.3f)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom){
            Button(
                onClick = { viewModel.changePasswordScreen() },
                shape = CircleShape,
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .padding(vertical = 10.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)
            ) {
                Text(
                    text = stringResource(R.string.profile_forget),
                    color = MaterialTheme.colors.background,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Row(
            Modifier
                .fillMaxHeight(0.1f)
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
fun ChangePassword(uiState: ProfileUiState, viewModel: ProfileViewModel, onDismissRequest: () -> Unit){
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp)
                .padding(16.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(MaterialTheme.colors.secondary)) {
            OutlinedTextField(
                value = uiState.oldPassword,
                onValueChange = {viewModel.updateOldPassword(it)},
                Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp),
                shape = RoundedCornerShape(30.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colors.background, unfocusedContainerColor = MaterialTheme.colors.background,
                    focusedBorderColor = MaterialTheme.colors.background, unfocusedBorderColor = MaterialTheme.colors.background),
                placeholder = {Text(text = stringResource(R.string.profile_oldpassword), color = MaterialTheme.colors.surface)})

            OutlinedTextField(
                value = uiState.newPassword,
                onValueChange = {viewModel.updateNewPassword(it)},
                Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp),
                shape = RoundedCornerShape(30.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colors.background, unfocusedContainerColor = MaterialTheme.colors.background,
                    focusedBorderColor = MaterialTheme.colors.background, unfocusedBorderColor = MaterialTheme.colors.background),
                placeholder = {Text(text = stringResource(R.string.profile_newpassword), color = MaterialTheme.colors.surface)})

            OutlinedTextField(
                value = uiState.confirmPassword,
                onValueChange = {viewModel.updateConfirmPassword(it)},
                Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp),
                shape = RoundedCornerShape(30.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colors.background, unfocusedContainerColor = MaterialTheme.colors.background,
                    focusedBorderColor = MaterialTheme.colors.background, unfocusedBorderColor = MaterialTheme.colors.background),
                placeholder = {Text(text = stringResource(R.string.profile_confirmpassword), color = MaterialTheme.colors.surface)})


            Row(Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically){
                Button(onClick = { viewModel.changePassword()},
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .border(3.dp, colorResource(R.color.white), RoundedCornerShape(30.dp)),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)) {
                    Text(text = stringResource(R.string.profile_save), color = MaterialTheme.colors.background,
                        fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
