package com.coolgirl.majko.data.dataStore

import androidx.datastore.preferences.preferencesKey

object UserScheme {
    val EMAIL = preferencesKey<String>("email")
    val PASSWORD = preferencesKey<String>("password")
    val IS_AUTORIZE = preferencesKey<Boolean>("is_autorize")
    val ID = preferencesKey<Int>("id")
}