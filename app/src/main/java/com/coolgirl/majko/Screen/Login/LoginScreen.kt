package com.coolgirl.majko.Screen.Login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.coolgirl.majko.R
import com.coolgirl.majko.components.BlueRoundedButton
import com.coolgirl.majko.components.ErrorSnackbar
import com.coolgirl.majko.components.LineTextField
import com.coolgirl.majko.navigation.Screen
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(navController: NavController){
    val viewModel: LoginViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    SetLoginScreen(navController, viewModel, uiState)

    Box(Modifier.fillMaxSize()) {
        if(uiState.isError){
            Row(Modifier.align(Alignment.BottomCenter)) {
                uiState.errorMessage?.let { ErrorSnackbar(it, { viewModel.isError(null) }) }
            }
        }
    }
}

@Composable
fun SetLoginScreen(navController: NavController, viewModel: LoginViewModel, uiState: LoginUiState){
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
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
                    color = MaterialTheme.colors.secondary,
                    shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Spacer(modifier = Modifier.height(1.dp))
            Column(
                Modifier
                    .fillMaxHeight(0.55f)
                    .fillMaxWidth(0.8f)
                    .padding(bottom = 60.dp)
                    .background(
                        color = MaterialTheme.colors.background,
                        shape = RoundedCornerShape(30.dp)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {

                Column(modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly){

                    LineTextField(uiState.userLogin, {viewModel.updateUserLogin(it)},
                        placeholder = stringResource(R.string.login_login))

                    LineTextField(uiState.userPassword, {viewModel.updateUserPassword(it)},
                        placeholder = stringResource(R.string.login_password))

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

        BlueRoundedButton({ viewModel.signIn(navController) }, stringResource(R.string.login_enter),
            modifier = Modifier.fillMaxWidth(0.73f), rounded = 15)

        Spacer(modifier = Modifier.height(10.dp))
        Text(text = stringResource(R.string.login_registrationoffer),
            modifier = Modifier.clickable { navController.navigate(Screen.Register.route) })
    }
}
