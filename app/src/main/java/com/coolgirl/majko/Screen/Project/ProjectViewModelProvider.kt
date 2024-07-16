package com.coolgirl.majko.Screen.Project

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.coolgirl.majko.data.dataStore.UserDataStore

class ProjectViewModelProvider(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ProjectViewModel::class.java)){
            return ProjectViewModel(UserDataStore(context)) as T
        }
        throw IllegalArgumentException("Ой ошибка")
    }

    companion object{
        fun getInstance(context: Context): ProjectViewModelProvider{
            return ProjectViewModelProvider(context.applicationContext)
        }
    }

}