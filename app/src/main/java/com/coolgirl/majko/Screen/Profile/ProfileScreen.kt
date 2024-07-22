package com.coolgirl.majko.Screen.Profile

import android.net.Uri
import android.provider.ContactsContract.Profile
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.coolgirl.majko.navigation.BottomBar
import com.coolgirl.majko.navigation.BottomBarScreens
import com.coolgirl.majko.R
import com.coolgirl.majko.Screen.Project.ProjectUiState
import com.coolgirl.majko.Screen.Project.ProjectViewModel
import com.coolgirl.majko.navigation.Screen
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen( navController: NavHostController) {
    val viewModel: ProfileViewModel = viewModel(key = "profileViewModel",
        factory = ProfileViewModelProvider.getInstance(LocalContext.current))

    val uiState by viewModel.uiState.collectAsState()
    Column(
        Modifier
            .fillMaxSize()
            .alpha(uiState.is_adding_background)
            .background(colorResource(R.color.white))) {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.93f)) {
            SetProfileScreen(uiState, {viewModel.updateUserName(it)}, {viewModel.updateUserEmail(it)}, viewModel, navController)
        }
        BottomBar(navController, listOf(
            BottomBarScreens.Notifications,
            BottomBarScreens.Task,
            BottomBarScreens.Profile))
    }

    if (uiState.is_adding){
        ChangePassword(uiState, viewModel)
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
                    focusedContainerColor = colorResource(R.color.white),
                    unfocusedContainerColor = colorResource(R.color.white)
                )
            )
            IconButton(onClick = { viewModel.updateNameData()  }) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "Сохранить",
                    tint = colorResource(R.color.blue)
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
                    focusedContainerColor = colorResource(R.color.white),
                    unfocusedContainerColor = colorResource(R.color.white)
                )
            )

            IconButton(onClick = { viewModel.updateEmailData() }) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "Сохранить",
                    tint = colorResource(R.color.blue)
                )
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
                    .padding(0.dp, 10.dp),
                colors = ButtonDefaults.buttonColors(colorResource(R.color.blue))
            ) {
                Text(
                    text = stringResource(R.string.profile_forget),
                    color = colorResource(R.color.white),
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
                color = colorResource(R.color.blue),
                modifier = Modifier.clickable {
                    viewModel.forgetAccount()
                    navController.navigate(Screen.Login.route)
                })

        }
    }
}

@Composable
fun ChangePassword(uiState: ProfileUiState, viewModel: ProfileViewModel){
    Column(Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Column(
            Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.45f)
                .clip(RoundedCornerShape(25.dp))
                .background(colorResource(R.color.purple)),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start) {

            OutlinedTextField(
                value = uiState.oldPassword,
                onValueChange = {viewModel.updateOldPassword(it)},
                Modifier.padding(20.dp, 20.dp, 0.dp, 0.dp),
                shape = RoundedCornerShape(30.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = colorResource(R.color.white), unfocusedContainerColor = colorResource(R.color.white),
                    focusedBorderColor = colorResource(R.color.white), unfocusedBorderColor = colorResource(R.color.white)),
                placeholder = {Text(text = stringResource(R.string.profile_oldpassword), color = colorResource(R.color.gray))})

            OutlinedTextField(
                value = uiState.newPassword,
                onValueChange = {viewModel.updateNewPassword(it)},
                Modifier.padding(20.dp, 20.dp, 0.dp, 0.dp),
                shape = RoundedCornerShape(30.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = colorResource(R.color.white), unfocusedContainerColor = colorResource(R.color.white),
                    focusedBorderColor = colorResource(R.color.white), unfocusedBorderColor = colorResource(R.color.white)),
                placeholder = {Text(text = stringResource(R.string.profile_newpassword), color = colorResource(R.color.gray))})

            OutlinedTextField(
                value = uiState.confirmPassword,
                onValueChange = {viewModel.updateConfirmPassword(it)},
                Modifier.padding(20.dp, 20.dp, 0.dp, 0.dp),
                shape = RoundedCornerShape(30.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = colorResource(R.color.white), unfocusedContainerColor = colorResource(R.color.white),
                    focusedBorderColor = colorResource(R.color.white), unfocusedBorderColor = colorResource(R.color.white)),
                placeholder = {Text(text = stringResource(R.string.profile_confirmpassword), color = colorResource(R.color.gray))})


            Row(Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically){
                Button(onClick = { viewModel.changePassword()},
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .border(3.dp, colorResource(R.color.white), RoundedCornerShape(30.dp)),
                    colors = ButtonDefaults.buttonColors(colorResource(R.color.blue))) {
                    Text(text = stringResource(R.string.profile_save), color = colorResource(R.color.white),
                        fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }


        }
    }
}
