package com.coolgirl.majko.navigation

//import androidx.compose.material3.*
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.coolgirl.majko.R

@Composable
fun BottomBar(navHostController: NavHostController, screensBottomBar: List<BottomBarScreens>){

    val screens = screensBottomBar

    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(containerColor = colorResource(R.color.white)) {
        screens.forEachIndexed { index, bottomBarScreens ->
            if (currentDestination != null){
                NavigationBarItem(
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colorResource(R.color.blue),
                        indicatorColor = colorResource(R.color.white),
                        selectedTextColor =  colorResource(R.color.blue),
                    ),
                    icon = { Icon(painter = painterResource(id = bottomBarScreens.icon), contentDescription = null, modifier = Modifier.size(20.dp))},
                    label = { Text(text = bottomBarScreens.title)},
                    selected = currentDestination!!.hierarchy.any{
                        it.route == bottomBarScreens.route
                    },
                    onClick = { navHostController.navigate(bottomBarScreens.route) }
                )
            }
        }
    }
}