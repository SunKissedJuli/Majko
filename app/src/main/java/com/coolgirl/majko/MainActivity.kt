package com.coolgirl.majko

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.createDataStore
import com.coolgirl.majko.data.dataStore.UserDataStore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UserDataStore.InitDataStore(LocalContext.current.createDataStore(name = "user_data_store"), rememberCoroutineScope())
            IsAutorize()

        }
    }
}
