package com.coolgirl.majko.Screen.TaskEditor

import android.util.Log
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.coolgirl.majko.components.SpinnerItems
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import com.coolgirl.majko.R
import com.coolgirl.majko.data.remote.ApiError
import com.coolgirl.majko.data.remote.ApiExeption
import com.coolgirl.majko.data.remote.ApiSuccess
import com.coolgirl.majko.data.remote.dto.NoteData.NoteById
import com.coolgirl.majko.data.remote.dto.NoteData.NoteData
import com.coolgirl.majko.data.remote.dto.NoteData.NoteUpdate
import com.coolgirl.majko.data.remote.dto.TaskData.*
import com.coolgirl.majko.data.repository.MajkoInfoRepository
import com.coolgirl.majko.data.repository.MajkoTaskRepository
import com.coolgirl.majko.navigation.Screen

class TaskEditorViewModel(private val majkoRepository: MajkoTaskRepository,
    private val majkoInfoRepository: MajkoInfoRepository) : ViewModel(){
    private val _uiState = MutableStateFlow(TaskEditorUiState.default())
    val uiState: StateFlow<TaskEditorUiState> = _uiState.asStateFlow()

    fun updateTaskText(text: String) {
        _uiState.update { it.copy(taskText = text) }
    }

    fun updateTaskName(name:String){
        _uiState.update { it.copy(taskName = name) }
    }

    fun updateTaskPriority(prioryti:String){
        _uiState.update { it.copy(taskPriority = prioryti.toInt()) }
        when (prioryti.toInt()) {
            1 -> _uiState.update { it.copy(backgroundColor = R.color.green) }
            2 -> _uiState.update { it.copy(backgroundColor = R.color.yellow) }
            3 -> _uiState.update { it.copy(backgroundColor = R.color.orange) }
            4 -> _uiState.update { it.copy(backgroundColor = R.color.red) }
            else -> _uiState.update { it.copy(backgroundColor = R.color.white) }
        }
    }

    fun updateTaskStatus(status:String){
        _uiState.update { it.copy(taskStatus = status.toInt()) }
    }

    fun updateNoteText(note:String){
        _uiState.update { it.copy(noteText = note) }
    }

    fun updateExpanded(){
        if(uiState.value.expanded){
            _uiState.update { it.copy(expanded = false)}
        }else{
            _uiState.update { it.copy(expanded = true)}
        }
    }

    fun updateExitDialog(){
        if(uiState.value.exitDialog){
            _uiState.update { it.copy(exitDialog = false) }
        }
        else{
            _uiState.update { it.copy(exitDialog = true) }
        }

    }

    fun updateSubtaskText(text: String) {
        _uiState.update { it.copy(subtaskText = text) }
    }

    fun updateSubtaskName(name:String){
        _uiState.update { it.copy(subtaskName = name) }
    }

    fun updateSubtaskStatus(status:String){
        _uiState.update { it.copy(subtaskStatus = status.toInt()) }
    }

    fun updateSubtaskDeadlie(deadline:String){
        _uiState.update { it.copy(subtaskDeadline = deadline) }
    }

    fun updateSubtaskPriority(prioryti:String){
        _uiState.update { it.copy(subtaskPriority = prioryti.toInt()) }
    }

    fun updateOldNoteText(noteText:String, id: String){
        _uiState.update { currentState ->
            val updatedNotes = currentState.notes?.map { note ->
                if (note.id == id) {
                    note.copy(note = noteText)
                } else {
                    note
                }
            }
            currentState.copy(
                notes = updatedNotes,
                noteText = noteText
            )
        }
    }

    fun addingTask(){
        if(uiState.value.isAdding){
            _uiState.update { it.copy(isAdding = false) }
        }else{
            _uiState.update { it.copy(isAdding = true) }
        }

    }

    fun addNewNote(){
        if(!uiState.value.newNote){
            _uiState.update { it.copy(newNote = true) }
        }else{
            _uiState.update { it.copy(newNote = false, noteText = "") }
        }

    }

    fun updateTaskDeadlie(deadline:String){
        _uiState.update { it.copy(taskDeadline = deadline) }
    }

    fun getStatus() : List<SpinnerItems>{
        val list = mutableListOf<SpinnerItems>()
        if (!uiState.value.statuses.isNullOrEmpty()) {
            for (status in uiState.value.statuses!!) {
                list.add(SpinnerItems(status.id.toString(), status.name))
            }
        }
        return list
    }

    fun getPriority() : List<SpinnerItems>{
        val list = mutableListOf<SpinnerItems>()
        if (!uiState.value.proprieties.isNullOrEmpty()) {
            for (propriety in uiState.value.proprieties!!) {
                list.add(SpinnerItems(propriety.id.toString(), propriety.name))
            }
        }
        return list
    }
    fun getStatusName(statusId: Int): String {
        return if (!uiState.value.statuses.isNullOrEmpty()) {
            uiState.value.statuses.find { it.id == statusId }?.name ?:  R.string.common_no.toString()
        } else {
            ""
        }
    }

    fun getPriority(priorityId: Int): Int {
        return when (priorityId) {
            1 -> R.color.green
            2 -> R.color.yellow
            3 -> R.color.orange
            4 -> R.color.red
            else -> R.color.white
        }
    }

    fun getPriorityName(priorityId: Int) : String{
        if(!uiState.value.proprieties.isNullOrEmpty()){
            for(item in uiState.value.proprieties!!){
                if(item.id==priorityId){
                    return item.name
                }
            }
        }
        return ""
    }

    fun saveUpdateNote(noteId: String, noteText: String){
        viewModelScope.launch {
            majkoRepository.updateNote(NoteUpdate(noteId,uiState.value.taskId, noteText)).collect() { response ->
                when(response){
                    is ApiSuccess ->{ loadNotesData() }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

    fun removeNote(noteId: String){
        viewModelScope.launch {
            majkoRepository.removeNote(NoteById(noteId)).collect() { response ->
                when(response){
                    is ApiSuccess ->{  loadNotesData() }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

    fun saveTask(navHostController: NavHostController, newTask: TaskUpdateData){
        if(!uiState.value.taskId.equals("0")){
           updateTask(navHostController, newTask)
        }else{
            navHostController.popBackStack()
            viewModelScope.launch {
                val newTask = TaskData(uiState.value.taskName, uiState.value.taskText,uiState.value.taskDeadline,
                    uiState.value.taskPriority,uiState.value.taskStatus,uiState.value.taskProject,"")
                majkoRepository.postNewTask(newTask).collect() { response ->
                    when(response){
                        is ApiSuccess -> { }
                        is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                        is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                    }
                }
            }
        }
    }

    fun updateTask(navHostController: NavHostController, newTask: TaskUpdateData){
        navHostController.popBackStack()
        viewModelScope.launch {
            majkoRepository.updateTask(newTask).collect() { response ->
                when(response){
                    is ApiSuccess -> { }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

    fun saveSubtask(newTask: TaskData){
        viewModelScope.launch {
            majkoRepository.postNewTask(newTask).collect() { response ->
                when(response){
                    is ApiSuccess ->{
                        addingTask()
                        loadSubtaskData()
                    }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

    fun removeTask(navHostController: NavHostController){
        navHostController.popBackStack()
       if(!uiState.value.taskId.equals("0")) {
           viewModelScope.launch {
               majkoRepository.removeTask(TaskByIdUnderscore(uiState.value.taskId)).collect() { response ->
                   when(response){
                       is ApiSuccess ->{}
                       is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                       is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                   }
               }
           }
       }
    }

    fun loadData(taskId: String) {
        _uiState.update { it.copy(taskId = taskId) }
        if(!taskId.equals("0")){
            viewModelScope.launch {
                majkoRepository.getTaskById(TaskById(uiState.value.taskId)).collect() { response ->
                    when(response){
                        is ApiSuccess ->{

                            updateTaskPriority(response.data.priority.toString())
                            if(response.data.countNotes!=0){ loadNotesData() }
                            if(response.data.countSubtasks!=0){ loadSubtaskData() }

                            _uiState.update { it.copy(
                                taskId = taskId,
                                taskDeadline = response.data.deadline,
                                taskName = response.data.title,
                                taskText = response.data.text,
                                taskStatus = response.data.status,
                                taskPriority = response.data.priority,
                                taskProjectObj = response.data.project,
                                taskProject = response.data.project.id
                                ) }

                            loadStatuses()
                            loadPriorities()
                        }
                        is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                        is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                    }
                }
            }
        }
        else{
            loadStatuses()
            loadPriorities()
        }
    }


    private fun loadStatuses(){
        viewModelScope.launch {
            majkoInfoRepository.getStatuses().collect() { response ->
                when(response){
                    is ApiSuccess ->{
                        _uiState.update { it.copy(statuses = response.data)}
                        _uiState.update { it.copy(taskStatusName = getStatusName(uiState.value.taskStatus)) }
                    }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

    private fun loadPriorities(){
        viewModelScope.launch {
            majkoInfoRepository.getPriorities().collect() { response ->
                when(response){
                    is ApiSuccess ->{
                        _uiState.update { it.copy(proprieties = response.data)}
                        _uiState.update {it.copy(taskPriorityName = getPriorityName(uiState.value.taskPriority)) }
                    }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

    fun addNote(note: NoteData){
        viewModelScope.launch {
            majkoRepository.addNote(note).collect() { response ->
                when(response){
                    is ApiSuccess ->{
                        addNewNote()
                        loadNotesData()
                    }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

    private fun loadSubtaskData() {
        if(!uiState.value.taskId.equals("0")){
            viewModelScope.launch {
                majkoRepository.getSubtask(TaskById(uiState.value.taskId)).collect() { response ->
                    when(response){
                        is ApiSuccess ->{ _uiState.update { it.copy(subtask = response.data) }}
                        is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                        is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                    }
                }
            }
        }
    }

    private fun loadNotesData(){
        viewModelScope.launch {
            majkoRepository.getNotes(TaskById(uiState.value.taskId)).collect() { response ->
                when(response){
                    is ApiSuccess ->{ _uiState.update { it.copy(notes = response.data) } }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }
}