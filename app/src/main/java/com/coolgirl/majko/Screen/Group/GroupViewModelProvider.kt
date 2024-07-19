package com.coolgirl.majko.Screen.Group

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.coolgirl.majko.Screen.Project.ProjectViewModel
import com.coolgirl.majko.data.dataStore.UserDataStore

class GroupViewModelProvider(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(GroupViewModel::class.java)){
            return GroupViewModel(UserDataStore(context)) as T
        }
        throw IllegalArgumentException("Ой ошибка")
    }

    companion object{
        fun getInstance(context: Context): GroupViewModelProvider{
            return GroupViewModelProvider(context.applicationContext)
        }
    }

}