package com.coolgirl.majko.Screen.TaskEditor

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.coolgirl.majko.components.SpinnerItems
import com.coolgirl.majko.data.dataStore.UserDataStore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import com.coolgirl.majko.R
import com.coolgirl.majko.commons.ApiError
import com.coolgirl.majko.commons.ApiExeption
import com.coolgirl.majko.commons.ApiSuccess
import com.coolgirl.majko.data.remote.dto.NoteData.NoteById
import com.coolgirl.majko.data.remote.dto.NoteData.NoteData
import com.coolgirl.majko.data.remote.dto.NoteData.NoteUpdate
import com.coolgirl.majko.data.remote.dto.TaskData.*
import com.coolgirl.majko.data.repository.MajkoInfoRepository
import com.coolgirl.majko.data.repository.MajkoTaskRepository
import com.coolgirl.majko.navigation.Screen

class TaskEditorViewModel(private val majkoRepository: MajkoTaskRepository,
    private val majkoInfoRepository: MajkoInfoRepository, private val taskId : String) : ViewModel(){
    private val _uiState = MutableStateFlow(TaskEditorUiState())
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
            2 -> _uiState.update { it.copy(backgroundColor = R.color.orange) }
            3 -> _uiState.update { it.copy(backgroundColor = R.color.red) }
            else -> _uiState.update { it.copy(backgroundColor = R.color.white) }
        }
    }

    fun updateTaskStatus(status:String){
        _uiState.update { it.copy(taskStatus = status.toInt()) }
    }

    fun updateNoteText(note:String){
        _uiState.update { it.copy(noteText = note) }
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
            _uiState.update { it.copy(newNote = false) }
            _uiState.update { it.copy(noteText = "") }
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

    fun getStatusName(statusId: Int) : String{
        if(!uiState.value.statuses.isNullOrEmpty()){
            for(item in uiState.value.statuses!!){
                if(item.id==statusId){
                    return item.name
                }
            }
        }
        return "Нет"
    }

    fun getPriority(priorityId: Int): Int{
        return when (priorityId) {
            1 -> R.color.green
            2 -> R.color.orange
            3 -> R.color.red
            else -> R.color.white
        }
    }

    fun getStatus(statusId: Int): String{
        if(!uiState.value.statuses.isNullOrEmpty()){
            for(item in uiState.value.statuses!!){
                if(item.id==statusId){
                    return item.name
                }
            }
        }
        return "Нет"
    }

    fun getPriorityName(priorityId: Int) : String{
        if(!uiState.value.proprieties.isNullOrEmpty()){
            for(item in uiState.value.proprieties!!){
                if(item.id==priorityId){
                    return item.name
                }
            }
        }
        return "Нет"
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

    fun saveTask(navHostController: NavHostController){
        if(!taskId.equals("0")){
           updateTask(navHostController)
        }else{
            viewModelScope.launch {
                val newTask = TaskData(uiState.value.taskName, uiState.value.taskText,uiState.value.taskDeadline,
                    uiState.value.taskPriority,uiState.value.taskStatus,uiState.value.taskProject,"")
                majkoRepository.postNewTask(newTask).collect() { response ->
                    when(response){
                        is ApiSuccess ->{ navHostController.navigate(Screen.Task.route) }
                        is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                        is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                    }
                }
            }
        }
    }

    fun updateTask(navHostController: NavHostController){
        viewModelScope.launch {
            val newTask = TaskUpdateData(uiState.value.taskId, uiState.value.taskName, uiState.value.taskText,
                uiState.value.taskPriority,uiState.value.taskDeadline, uiState.value.taskStatus)
            majkoRepository.updateTask(newTask).collect() { response ->
                when(response){
                    is ApiSuccess ->{ navHostController.navigate(Screen.Task.route) }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

    fun saveSubtask(){
        viewModelScope.launch {
            val newTask = TaskData(uiState.value.subtaskName, uiState.value.subtaskText,uiState.value.subtaskDeadline,
                uiState.value.subtaskPriority,uiState.value.subtaskStatus,"",uiState.value.taskId)
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
       if(!taskId.equals("0")) {
           viewModelScope.launch {
               majkoRepository.removeTask(TaskBy_Id(taskId)).collect() { response ->
                   when(response){
                       is ApiSuccess ->{ navHostController.navigate(Screen.Task.route) }
                       is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                       is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                   }
               }
           }
       }
    }

    fun loadData() {
        loadStatuses()
        loadPriorities()

        _uiState.update { it.copy(taskId = taskId) }

        if(!taskId.equals("0")){
            viewModelScope.launch {
                majkoRepository.getTaskById(TaskById(uiState.value.taskId)).collect() { response ->
                    when(response){
                        is ApiSuccess ->{
                            _uiState.update { it.copy(taskId = taskId) }
                            _uiState.update { it.copy(taskDeadline = response.data!!.deadline) }
                            if(response.data?.project !=null){
                                _uiState.update { it.copy(taskProject = response.data!!.project!!.id) }
                            }
                            updateTaskPriority(response.data!!.priority!!.toString())
                            _uiState.update { it.copy(taskName = response.data!!.title!!) }
                            _uiState.update { it.copy(taskText = response.data!!.text!!) }
                            _uiState.update { it.copy(taskStatus = response.data!!.status!!) }
                            _uiState.update { it.copy(taskPriority = response.data!!.priority!!) }
                            _uiState.update { it.copy(taskPriorityName = getPriorityName(response.data!!.priority!!)) }
                            _uiState.update { it.copy(taskStatusName = getStatusName(response.data!!.status!!)) }
                            if(response.data!!.project!=null){
                                _uiState.update { it.copy(taskProjectObj = response.data!!.project!!) }
                            }

                            if(response.data!!.count_notes!=0){
                                loadNotesData()
                            }
                            if(response.data!!.count_subtasks!=0){
                                loadSubtaskData()
                            }
                        }
                        is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                        is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                    }
                }
            }
        }
    }


    fun loadStatuses(){
        viewModelScope.launch {
            majkoInfoRepository.getStatuses().collect() { response ->
                when(response){
                    is ApiSuccess ->{
                        _uiState.update { it.copy(statuses = response.data!!) }
                    }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

    fun loadPriorities(){
        viewModelScope.launch {
            majkoInfoRepository.getPriorities().collect() { response ->
                when(response){
                    is ApiSuccess ->{
                        _uiState.update { it.copy(proprieties = response.data!!) }
                    }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

    fun addNote(){
        viewModelScope.launch {
            majkoRepository.addNote(NoteData(uiState.value.taskId, uiState.value.noteText)).collect() { response ->
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

    fun loadSubtaskData() {
        if(!taskId.equals("0")){
            _uiState.update { it.copy(taskId = taskId) }
            viewModelScope.launch {
                majkoRepository.getSubtask(TaskById(uiState.value.taskId)).collect() { response ->
                    when(response){
                        is ApiSuccess ->{ _uiState.update { it.copy(subtask = response.data!!) }}
                        is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                        is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                    }
                }
            }
        }
    }

    fun loadNotesData(){
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