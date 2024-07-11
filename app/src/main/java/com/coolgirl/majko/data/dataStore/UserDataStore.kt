package com.coolgirl.majko.data.dataStore

import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import kotlinx.coroutines.CoroutineScope

object UserDataStore{
    private var userDataStore : DataStore<Preferences>? = null
    var coroutineScope : CoroutineScope? = null

    fun InitDataStore(initDataStore : DataStore<Preferences>, initCoroutineScope: CoroutineScope){
        userDataStore = initDataStore
        coroutineScope = initCoroutineScope
    }

    fun GetDataStore() : DataStore<Preferences>? {
        return userDataStore
    }
}
