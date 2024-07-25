package com.coolgirl.majko.Screen.TaskEditor

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.coolgirl.majko.components.SpinnerItems
import com.coolgirl.majko.data.dataStore.UserDataStore
import com.coolgirl.majko.di.ApiClient
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import androidx.lifecycle.viewModelScope
import com.coolgirl.majko.R
import com.coolgirl.majko.commons.ApiError
import com.coolgirl.majko.commons.ApiExeption
import com.coolgirl.majko.commons.ApiSuccess
import com.coolgirl.majko.data.MajkoRepository
import com.coolgirl.majko.data.remote.dto.NoteData.NoteById
import com.coolgirl.majko.data.remote.dto.NoteData.NoteData
import com.coolgirl.majko.data.remote.dto.NoteData.NoteDataResponse
import com.coolgirl.majko.data.remote.dto.NoteData.NoteUpdate
import com.coolgirl.majko.data.remote.dto.TaskData.*
import com.coolgirl.majko.data.remote.dto.UserUpdateEmail
import com.coolgirl.majko.navigation.Screen
import retrofit2.Response

class TaskEditorViewModel(private val dataStore : UserDataStore, private val majkoRepository: MajkoRepository, private val task_id : String) : ViewModel(){
    private val _uiState = MutableStateFlow(TaskEditorUiState())
    val uiState: StateFlow<TaskEditorUiState> = _uiState.asStateFlow()

    init{loadData()}

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

    fun updateSubtaskProject(project:String){
        _uiState.update { it.copy(subtaskProject = project) }
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
        if(uiState.value.newNote==false){
            _uiState.update { it.copy(newNote = true) }
        }else{
            _uiState.update { it.copy(newNote = false) }
            _uiState.update { it.copy(noteText = "") }
        }

    }

    fun updateTaskProject(project:String){
        _uiState.update { it.copy(taskProject = project) }
    }

    fun updateTaskDeadlie(deadline:String){
        _uiState.update { it.copy(taskDeadline = deadline) }
    }

    fun getStatus() : List<SpinnerItems>{
        return listOf(
            SpinnerItems("1", "Не выбрано"),
            SpinnerItems("2", "Обсуждается"),
            SpinnerItems("3", "Ожидает"),
            SpinnerItems("4", "В процессе"),
            SpinnerItems("5", "Завершена")
        )
    }



    fun getPriority() : List<SpinnerItems>{
        return listOf(
            SpinnerItems("1", "Низкий"),
            SpinnerItems("2", "Средний"),
            SpinnerItems("3", "Высокий")
        )
    }

    fun getStatusName(status: Int) : String{
        return when (status) {
            1 -> "Не выбрано"
            2 -> "Обсуждается"
            3 -> "Ожидает"
            4 -> "В процессе"
            5 -> "Завершена"
            else -> "Нет"
        }
    }

    fun getPriority(priorityId: Int): Int{
        return when (priorityId) {
            1 -> R.color.green
            2 -> R.color.orange
            3 -> R.color.red
            else -> R.color.white
        }
    }

    fun getStatus(priorityId: Int): String{
        return when (priorityId) {
            1 -> "Не выбрано"
            2 -> "Обсуждается"
            3 -> "Ожидает"
            4 -> "В процессе"
            5 -> "Завершена"
            else -> "Нет статуса"
        }
    }

    fun getPriorityName(priority: Int) : String{
        return when (priority) {
            1 -> "Низкий"
            2 -> "Средний"
            3 -> "Высокий"
            else -> "Нет"
        }
    }

    fun saveUpdateNote(noteId: String, noteText: String){
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            majkoRepository.updateNote("Bearer " + accessToken,  NoteUpdate(noteId,uiState.value.taskId, noteText)).collect() { response ->
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
            val accessToken = dataStore.getAccessToken().first() ?: ""
            majkoRepository.removeNote("Bearer " + accessToken,   NoteById(noteId)).collect() { response ->
                when(response){
                    is ApiSuccess ->{  loadNotesData() }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

    fun saveTask(navHostController: NavHostController){
        if(!task_id.equals("0")){
            viewModelScope.launch {
                val accessToken = dataStore.getAccessToken().first() ?: ""
                val newTask = TaskUpdateData(uiState.value.taskId, uiState.value.taskName, uiState.value.taskText,
                    uiState.value.taskPriority,uiState.value.taskDeadline, uiState.value.taskStatus)
                majkoRepository.updateTask("Bearer " + accessToken,  newTask).collect() { response ->
                    when(response){
                        is ApiSuccess ->{ navHostController.navigate(Screen.Task.route) }
                        is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                        is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                    }
                }
            }
        }else{
            viewModelScope.launch {
                val accessToken = dataStore.getAccessToken().first() ?: ""
                val newTask = TaskData(uiState.value.taskName, uiState.value.taskText,uiState.value.taskDeadline,
                    uiState.value.taskPriority,uiState.value.taskStatus,uiState.value.taskProject,"")
                majkoRepository.postNewTask("Bearer " + accessToken,  newTask).collect() { response ->
                    when(response){
                        is ApiSuccess ->{ navHostController.navigate(Screen.Task.route) }
                        is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                        is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                    }
                }
            }
        }

    }

    fun saveSubtask(){
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            val newTask = TaskData(uiState.value.subtaskName, uiState.value.subtaskText,uiState.value.subtaskDeadline,
                uiState.value.subtaskPriority,uiState.value.subtaskStatus,"",uiState.value.taskId)
            majkoRepository.postNewTask("Bearer " + accessToken,  newTask).collect() { response ->
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
       if(!task_id.equals("0")) {
           viewModelScope.launch {
               val accessToken = dataStore.getAccessToken().first() ?: ""
               majkoRepository.removeTask("Bearer " + accessToken, TaskBy_Id(task_id)).collect() { response ->
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
        if(!task_id.equals("0")){
            _uiState.update { it.copy(taskId = task_id) }
            viewModelScope.launch {
                val accessToken = dataStore.getAccessToken().first() ?: ""
                majkoRepository.getTaskById("Bearer " + accessToken, TaskById(uiState.value.taskId)).collect() { response ->
                    when(response){
                        is ApiSuccess ->{
                            _uiState.update { it.copy(taskId = task_id) }
                            _uiState.update { it.copy(taskDeadline = response.data!!.deadline) }
                            if(response.data?.project !=null){
                                _uiState.update { it.copy(taskProject = response.data!!.project!!.id) }
                            }
                            updateTaskPriority(response.data!!.priority!!.toString())
                            _uiState.update { it.copy(taskName = response.data!!.title!!) }
                            _uiState.update { it.copy(taskText = response.data!!.text!!) }
                            _uiState.update { it.copy(taskStatus = response.data!!.status!!) }
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

    fun addNote(){
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            majkoRepository.addNote("Bearer " + accessToken,  NoteData(uiState.value.taskId, uiState.value.noteText)).collect() { response ->
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
        if(!task_id.equals("0")){
            _uiState.update { it.copy(taskId = task_id) }
            viewModelScope.launch {
                val accessToken = dataStore.getAccessToken().first() ?: ""
                majkoRepository.getSubtask("Bearer " + accessToken,  TaskById(uiState.value.taskId)).collect() { response ->
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
            val accessToken = dataStore.getAccessToken().first() ?: ""
            majkoRepository.getNotes("Bearer " + accessToken, TaskById(uiState.value.taskId)).collect() { response ->
                when(response){
                    is ApiSuccess ->{ _uiState.update { it.copy(notes = response.data) } }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }
}