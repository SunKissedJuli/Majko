package com.coolgirl.majko

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import com.coolgirl.majko.Screen.Profile.ProfileViewModel
import com.coolgirl.majko.commons.IsAutorize
import com.coolgirl.majko.data.dataStore.UserDataStore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IsAutorize()
        }
    }
}
