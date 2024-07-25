package com.coolgirl.majko.Screen.Group

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coolgirl.majko.commons.ApiError
import com.coolgirl.majko.commons.ApiExeption
import com.coolgirl.majko.commons.ApiSuccess
import com.coolgirl.majko.data.MajkoRepository
import com.coolgirl.majko.data.dataStore.UserDataStore
import com.coolgirl.majko.data.remote.dto.GroupData.GroupBy_Id
import com.coolgirl.majko.data.remote.dto.GroupData.GroupData
import com.coolgirl.majko.data.remote.dto.GroupData.GroupResponse
import com.coolgirl.majko.data.remote.dto.MessageData
import com.coolgirl.majko.data.remote.dto.ProjectData.JoinByInviteProjectData
import com.coolgirl.majko.di.ApiClient
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupViewModel(private val dataStore: UserDataStore, private val majkoRepository: MajkoRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(GroupUiState())
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


    fun updateSearchString(newSearchString:String){
        _uiState.update { currentState ->
            val filteredPersonalGroup = currentState.personalGroup?.filter { task ->
                task.title?.contains(newSearchString, ignoreCase = true) == true ||
                        task.description?.contains(newSearchString, ignoreCase = true) == true
            }

            val filteredGroupGroup = currentState.groupGroup?.filter { task ->
                task.title?.contains(newSearchString, ignoreCase = true) == true ||
                        task.description?.contains(newSearchString, ignoreCase = true) == true
            }

            currentState.copy(
                searchString = newSearchString,
                searchPersonalGroup = filteredPersonalGroup,
                searchGroupGroup = filteredGroupGroup
            )
        }
    }


    fun addGroup(){
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            majkoRepository.addGroup("Bearer " + accessToken,  GroupData(uiState.value.newGroupName, uiState.value.newGroupDescription)).collect() { response ->
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

    fun loadData() {
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            majkoRepository.getPersonalGroup("Bearer " + accessToken).collect() { response ->
                when(response){
                    is ApiSuccess ->{
                        val validData: MutableList<GroupResponse> = mutableListOf()
                        response.data?.forEach { item ->
                            if (item.is_personal ) {
                                validData.add(item)
                            }
                        }
                        _uiState.update { it.copy(personalGroup = validData)}
                        _uiState.update { it.copy(searchPersonalGroup = validData)}
                    }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
            majkoRepository.getGroupGroup("Bearer " + accessToken).collect() { response ->
                when(response){
                    is ApiSuccess ->{
                        val validData: MutableList<GroupResponse> = mutableListOf()
                        response.data?.forEach { item ->
                            if (!item.is_personal) {
                                validData.add(item)
                            }
                        }
                        _uiState.update { it.copy(groupGroup = validData)}
                        _uiState.update { it.copy(searchGroupGroup = validData)}
                    }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

    fun joinByInvite(){
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            majkoRepository.joinGroupByInvitation("Bearer " + accessToken, JoinByInviteProjectData(uiState.value.invite)).collect() { response ->
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