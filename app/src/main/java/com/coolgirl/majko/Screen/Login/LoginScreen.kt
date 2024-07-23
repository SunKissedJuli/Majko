package com.coolgirl.majko.Screen.Login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.coolgirl.majko.R
import com.coolgirl.majko.data.dataStore.UserDataStore

@Composable
fun LoginScreen(navController: NavController){
    val dataStore : UserDataStore = UserDataStore(LocalContext.current)
    val viewModel:LoginViewModel = LoginViewModel(dataStore)
    SetLoginScreen(navController, viewModel)
}

@Composable
fun SetLoginScreen(navController: NavController, viewModel: LoginViewModel){
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
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Spacer(modifier = Modifier.height(1.dp))
            Column(
                Modifier
                    .fillMaxHeight(0.65f)
                    .fillMaxWidth(0.8f)
                    .background(color = MaterialTheme.colors.background, shape = RoundedCornerShape(30.dp)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Column(modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly){
                    key(viewModel.change){
                        if(!viewModel.isThisSignIn()){
                            TextField(
                                value = viewModel.userName,
                                modifier = Modifier.height(55.dp),
                                placeholder = { Text(text = stringResource(id = R.string.login_username), color = MaterialTheme.colors.surface) },
                                onValueChange = { viewModel.updateUserName(it) },
                                colors = TextFieldDefaults.colors(focusedContainerColor = MaterialTheme.colors.background,
                                    unfocusedContainerColor = MaterialTheme.colors.background)
                            )
                        }
                    }
                    TextField(
                        value = viewModel.userLogin,
                        modifier = Modifier.height(55.dp),
                        placeholder = { Text(text = stringResource(id = R.string.login_login), color = MaterialTheme.colors.surface) },
                        onValueChange = { viewModel.updateUserLogin(it) },
                        colors = TextFieldDefaults.colors(focusedContainerColor = MaterialTheme.colors.background,
                            unfocusedContainerColor = MaterialTheme.colors.background)
                    )
                    TextField(
                        value = viewModel.userPassword,
                        modifier = Modifier.height(55.dp),
                        placeholder = { Text(text = stringResource(id = R.string.login_password), color = MaterialTheme.colors.surface) },
                        onValueChange = { viewModel.updateUserPassword(it) },
                        colors = TextFieldDefaults.colors(focusedContainerColor = colorResource(R.color.white),
                            unfocusedContainerColor = MaterialTheme.colors.background)
                    )
                    key(viewModel.change){
                        if(!viewModel.isThisSignIn()){
                            TextField(
                            value = viewModel.userPasswordRepeat,
                            modifier = Modifier.height(55.dp),
                            placeholder = { Text(text = stringResource(id = R.string.login_passwordrepeat), color = MaterialTheme.colors.surface) },
                            onValueChange = { viewModel.updateUserPasswordRepeat(it) },
                            colors = TextFieldDefaults.colors(focusedContainerColor = MaterialTheme.colors.background,
                                unfocusedContainerColor = MaterialTheme.colors.background)
                           )
                        }
                    }
                }
            }
            key(viewModel.change){
                Button(onClick = { viewModel.signIn(navController) },
                    modifier = Modifier
                        .fillMaxWidth(0.73f),
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)) {
                    Text(text = stringResource(viewModel.enterButtonText()), color = MaterialTheme.colors.background,
                        fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                Text(text = stringResource(viewModel.bottomText()),
                    modifier = Modifier.clickable { viewModel.changeScreen() })
            }
        }
    }
}
