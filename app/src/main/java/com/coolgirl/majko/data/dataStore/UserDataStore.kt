package com.coolgirl.majko.data.dataStore

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserDataStore(private val context: Context){
    companion object {

        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "master_data_store")
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")

    }



    fun getAccessToken() : Flow<String?>{
        return  context.dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN].toString()
        }
    }

    suspend fun setAccesToken(accesToken:String){
        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = accesToken
        }
    }
}
