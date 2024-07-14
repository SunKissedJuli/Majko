package com.coolgirl.majko.Screen.TaskEditor

import androidx.navigation.NavHostController
import com.coolgirl.majko.Screen.Task.TaskUiState
import com.coolgirl.majko.data.dataStore.UserDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TaskEditorViewModel(private val dataStore : UserDataStore) {
    private val _uiState = MutableStateFlow(TaskEditorUiState())
    val uiState: StateFlow<TaskEditorUiState> = _uiState.asStateFlow()

    init{ loadData() }

    fun updateTaskText(text: String) {
        _uiState.update { it.copy(taskText = text) }
    }

    fun saveTask(navHostController: NavHostController){

    }

    fun loadData() {

    }
}