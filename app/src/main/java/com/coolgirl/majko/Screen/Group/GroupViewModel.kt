package com.coolgirl.majko.Screen.Group

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coolgirl.majko.R
import com.coolgirl.majko.data.remote.ApiError
import com.coolgirl.majko.data.remote.ApiExeption
import com.coolgirl.majko.data.remote.ApiSuccess
import com.coolgirl.majko.data.remote.dto.GroupData.GroupById
import com.coolgirl.majko.data.remote.dto.GroupData.GroupData
import com.coolgirl.majko.data.remote.dto.GroupData.GroupResponse
import com.coolgirl.majko.data.remote.dto.ProjectData.JoinByInviteProjectData
import com.coolgirl.majko.data.remote.dto.TaskData.SearchTask
import com.coolgirl.majko.data.repository.MajkoGroupRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GroupViewModel(private val majkoRepository: MajkoGroupRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(GroupUiState.default())
    val uiState: StateFlow<GroupUiState> = _uiState.asStateFlow()

    fun updateGroupName(name: String){
        _uiState.update { it.copy(newGroupName = name) }
    }

    fun updateGroupDescription(description: String){
        _uiState.update { it.copy(newGroupDescription = description) }
    }

    fun updateInvite(invite: String){
        _uiState.update { it.copy(invite = invite) }
    }

    fun addingGroup(){
        if(uiState.value.isAdding){
            _uiState.update { it.copy(isAdding = false)}
        }else{
            _uiState.update { it.copy(isAdding = true)}
        }
    }

    fun openInviteWindow(){
        if(!uiState.value.isInvite){
            _uiState.update { it.copy(isInvite = true)}
        }else{
            _uiState.update { it.copy(isInvite = false)}
        }
    }

    fun openPanel(id: String) {
        val idLength = 36
        val currentIds = uiState.value.longtapGroupId.chunked(idLength)
        val updatedIds = if (currentIds.contains(id)) {
            currentIds.filter { it != id }
        } else {
            currentIds + id
        }.joinToString("")

        _uiState.update { it.copy(isLongtap = updatedIds.isNotEmpty(), longtapGroupId = updatedIds) }
    }

    fun isError(message: Int?) {
        if (uiState.value.isError) {
            _uiState.update { it.copy(isError = false) }
        } else {
            _uiState.update { it.copy(errorMessage = message, isError = true) }
        }
    }

    fun isMessage(message: Int?) {
        if (uiState.value.isMessage) {
            _uiState.update { it.copy(isMessage = false) }
        } else {
            _uiState.update { it.copy(message = message, isMessage = true ) }
        }
    }

    fun updateSearchString(newSearchString:String, whatFilter: Int){
        when (whatFilter) {
            0 -> {  loadPersonalGroup(newSearchString)
                _uiState.update { it.copy(searchString = newSearchString, searchGroupGroup = listOf()) } }
            1 -> {  loadGroupGroup(newSearchString)
                _uiState.update { it.copy(searchString = newSearchString, searchPersonalGroup = listOf()) } }
            else -> {loadData(newSearchString)
                _uiState.update { it.copy(searchString = newSearchString) }  }
        }
    }

    fun updateExpandedFilter(){
        if(uiState.value.expandedFilter){
            _uiState.update { it.copy(expandedFilter = false)}
        }else{
            _uiState.update { it.copy(expandedFilter = true)}
        }
    }

    fun updateExpanded(){
        if(uiState.value.expanded){
            _uiState.update { it.copy(expanded = false)}
        }else{
            _uiState.update { it.copy(expanded = true)}
        }
    }

    fun updateExpandedLongTap(){
        if(uiState.value.expandedLongTap){
            _uiState.update { it.copy(expandedLongTap = false)}
        }else{
            _uiState.update { it.copy(expandedLongTap = true)}
        }
    }

    fun removeGroup() {
       val groupIds = uiState.value.longtapGroupId.chunked(36)
        groupIds.mapNotNull { id ->
            val group = uiState.value.groupGroup?.find { it.id == id }
                ?: uiState.value.personalGroup?.find { it.id == id }

            group?.let {
                val removeGroup = GroupById(it.id)

                viewModelScope.launch {
                    majkoRepository.removeGroup(removeGroup).collect { response ->
                        when (response) {
                            is ApiSuccess -> {
                                isMessage(R.string.message_remove_group)
                                loadData()
                            }
                            is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                            is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                        }
                    }
                }
            }
        }

        _uiState.update { it.copy(longtapGroupId = "", isLongtap = false) }
        loadData()
    }


    fun addGroup(){
        viewModelScope.launch {
            majkoRepository.addGroup(GroupData(uiState.value.newGroupName, uiState.value.newGroupDescription)).collect() { response ->
                when(response){
                    is ApiSuccess ->{
                        addingGroup()
                        loadData()}
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

    fun loadData(search: String = "") {
        loadPersonalGroup(search)
        loadGroupGroup(search)
    }

    private fun loadGroupGroup(search: String) {
        viewModelScope.launch {
            majkoRepository.getGroupGroup(SearchTask(search)).collect() { response ->
                when (response) {
                    is ApiSuccess -> {
                        val validData: MutableList<GroupResponse> = mutableListOf()
                        response.data?.forEach { item ->
                            if (!item.isPersonal) {
                                validData.add(item)
                            }
                        }
                        _uiState.update { it.copy(groupGroup = validData, searchGroupGroup = validData) }
                    }
                    is ApiError -> {
                        Log.d("TAG", "error message = " + response.message)
                    }
                    is ApiExeption -> {
                        Log.d("TAG", "exeption e = " + response.e)
                    }
                }
            }
        }
    }

    private fun loadPersonalGroup(search: String){
        viewModelScope.launch {
            majkoRepository.getPersonalGroup(SearchTask(search)).collect() { response ->
                when(response){
                    is ApiSuccess ->{
                        val validData: MutableList<GroupResponse> = mutableListOf()
                        response.data?.forEach { item ->
                            if (item.isPersonal ) {
                                validData.add(item)
                            }
                        }
                        _uiState.update { it.copy(personalGroup = validData, searchPersonalGroup = validData)}
                    }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

    fun joinByInvite(){
        viewModelScope.launch {
            majkoRepository.joinGroupByInvitation(JoinByInviteProjectData(uiState.value.invite)).collect() { response ->
                when(response){
                    is ApiSuccess ->{
                        _uiState.update { it.copy(inviteMessage = response.data!!.message!!) }
                        loadData()
                    }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }
}