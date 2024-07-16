package com.coolgirl.majko.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.coolgirl.majko.R
import kotlinx.coroutines.launch

@Composable
fun ModalNavigationDrawer(navHostController: NavHostController, screensModalNavigationDrawer: List<ModalNavigationDrawerScreens>) {
    val items = screensModalNavigationDrawer
    val (selectedItem, setSelectedItem) = remember { mutableStateOf(items[0]) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                Modifier.fillMaxWidth(0.7f),
                drawerContainerColor = colorResource(R.color.purple)) {
                items.forEach { item ->
                    NavigationDrawerItem(
                        label = { Text(item.title, fontSize = 22.sp) },
                        selected = selectedItem == item,
                        onClick = {
                            setSelectedItem(item)
                            navHostController.navigate(item.route)
                            scope.launch { drawerState.close() } },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = Color.Transparent, unselectedContainerColor = Color.Transparent,
                            selectedTextColor = Color.Black, unselectedTextColor = Color.White)
                    )
                }
            }
        },
        content = {
            Row(Modifier.padding(10.dp)) {
                IconButton(
                    onClick = { scope.launch { drawerState.open() } },
                    content = {
                        Icon(
                            Icons.Filled.Menu,
                            contentDescription = "Menu",
                            tint = colorResource(R.color.white))
                    }
                )
            }
        }
    )
}