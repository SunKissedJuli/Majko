package com.coolgirl.majko.Screen.Task

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.coolgirl.majko.data.dataStore.UserDataStore

class TaskViewModelProvider(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TaskViewModel::class.java)){
            return TaskViewModel(UserDataStore(context)) as T
        }
        throw IllegalArgumentException("Ой ошибка")
    }

    companion object{
        fun getInstance(context: Context): TaskViewModelProvider{
            return TaskViewModelProvider(context.applicationContext)
        }
    }

}