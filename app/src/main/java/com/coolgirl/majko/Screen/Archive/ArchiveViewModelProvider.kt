package com.coolgirl.majko.Screen.Archive

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.coolgirl.majko.data.dataStore.UserDataStore


class ArchiveViewModelProvider(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ArchiveViewModel::class.java)){
            return ArchiveViewModel(UserDataStore(context)) as T
        }
        throw IllegalArgumentException("Ой ошибка")
    }

    companion object{
        fun getInstance(context: Context): ArchiveViewModelProvider{
            return ArchiveViewModelProvider(context.applicationContext)
        }
    }

}