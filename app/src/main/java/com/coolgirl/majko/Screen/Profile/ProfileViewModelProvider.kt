package com.coolgirl.majko.Screen.Profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.coolgirl.majko.data.dataStore.UserDataStore

class ProfileViewModelProvider(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(UserDataStore(context)) as T
        }
        throw IllegalArgumentException("Ой ошибка")
    }

    companion object {
        fun getInstance(context: Context): ProfileViewModelProvider {
            return ProfileViewModelProvider(context.applicationContext)
        }
    }
}