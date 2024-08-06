package com.coolgirl.majko.Screen.Register

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.coolgirl.majko.R
import com.coolgirl.majko.components.BlueRoundedButton
import com.coolgirl.majko.components.ErrorSnackbar
import com.coolgirl.majko.components.LineTextField
import com.coolgirl.majko.data.remote.dto.UserSignUpData.UserSignUpData
import com.coolgirl.majko.navigation.Screen
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen(navController: NavController){
    val viewModel: RegisterViewModel = koinViewModel()

    val uiState by viewModel.uiState.collectAsState()

    SetRegisterScreen(navController, viewModel, uiState)

    Box(Modifier.fillMaxSize()) {
        if(uiState.isError){
            Row(Modifier.align(Alignment.BottomCenter)) {
                uiState.errorMessage?.let { ErrorSnackbar(it, { viewModel.isError(null) }) }
            }
        }
    }
}

@Composable
fun SetRegisterScreen(navController: NavController, viewModel: RegisterViewModel, uiState: RegisterUiState){
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top){
        Column(
            Modifier
                .fillMaxHeight(0.3f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center) {
            Row(){
                Spacer(modifier = Modifier.width(60.dp))
                Text(text = stringResource(R.string.app_name), fontSize = 35.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(15.dp))
            Row(){
                Spacer(modifier = Modifier.width(25.dp))
                Text(text = stringResource(R.string.login_welcomtext), fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
        }

        Column(
            Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Spacer(modifier = Modifier.height(1.dp))
            Column(
                Modifier
                    .fillMaxHeight(0.75f)
                    .fillMaxWidth(0.8f)
                    .padding(bottom = 60.dp)
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(30.dp)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Column(modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly){

                    LineTextField(uiState.userName, {viewModel.updateUserName(it)},
                        placeholder = stringResource(R.string.login_username))

                    LineTextField(uiState.userLogin, {viewModel.updateUserLogin(it)},
                        placeholder = stringResource(R.string.login_login))

                    LineTextField(uiState.userPassword, {viewModel.updateUserPassword(it)},
                        placeholder = stringResource(R.string.login_password))

                    LineTextField(uiState.userPasswordRepeat, {viewModel.updateUserPasswordRepeat(it)},
                        placeholder = stringResource(R.string.login_passwordrepeat))

                }
            }
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(bottom = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom) {

        BlueRoundedButton({ viewModel.signUp(navController,
            UserSignUpData(uiState.userLogin, uiState.userPassword, uiState.userName)) },
            stringResource(R.string.login_registration),
            modifier = Modifier.fillMaxWidth(0.73f), rounded = 15)

        Spacer(modifier = Modifier.height(10.dp))
        Text(text = stringResource(R.string.login_enteroffer),
            modifier = Modifier.clickable { navController.navigate(Screen.Login.route) })
    }
}