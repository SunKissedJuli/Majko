package com.coolgirl.majko.Screen.Login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.coolgirl.majko.R

@Composable
fun LoginScreen(navController: NavController){
    val viewModel:LoginViewModel = viewModel()
    SetLoginScreen(navController, viewModel)
}

@Composable
fun SetLoginScreen(navController: NavController, viewModel: LoginViewModel){
    Column(
        Modifier
            .fillMaxSize()
            .background(colorResource(R.color.white)),
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Top){
        Column(
            Modifier
                .fillMaxHeight(0.35f)
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
                    color = colorResource(R.color.purple),
                    shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Spacer(modifier = Modifier.height(1.dp))
            Column(
                Modifier
                    .fillMaxHeight(0.55f)
                    .fillMaxWidth(0.8f)
                    .background(color = colorResource(R.color.white), shape = RoundedCornerShape(30.dp)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Column(modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly){
                    TextField(
                        value = viewModel.userLogin,
                        placeholder = { Text(text = stringResource(id = R.string.login_login), color = colorResource(R.color.gray)) },
                        onValueChange = { viewModel.updateUserLogin(it) },
                        colors = TextFieldDefaults.colors(colorResource(R.color.white))
                    )
                    TextField(
                        value = viewModel.userPassword,
                        placeholder = { Text(text = stringResource(id = R.string.login_password), color = colorResource(R.color.gray)) },
                        onValueChange = { viewModel.updateUserPassword(it) },
                        colors = TextFieldDefaults.colors(colorResource(R.color.white)),
                    )
                    key(viewModel.change){
                        if(!viewModel.isThisSignIn()){
                            TextField(
                            value = viewModel.userPasswordRepeat,
                            placeholder = { Text(text = stringResource(id = R.string.login_passwordrepeat), color = colorResource(R.color.gray)) },
                            onValueChange = { viewModel.updateUserPasswordRepeat(it) },
                                colors = TextFieldDefaults.colors(colorResource(R.color.white))
                           )
                        }
                    }
                }
            }
            key(viewModel.change){
                Button(onClick = { viewModel.signIn(navController) },
                    modifier = Modifier
                        .fillMaxWidth(0.7f),
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.buttonColors(colorResource(R.color.blue))) {
                    Text(text = stringResource(viewModel.enterButtonText()), color = colorResource(R.color.white), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                Text(text = stringResource(viewModel.bottomText()),
                    modifier = Modifier.clickable { viewModel.changeScreen() })
            }
        }
    }
}
