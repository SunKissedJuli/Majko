package com.coolgirl.majko.Screen.Group

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coolgirl.majko.data.dataStore.UserDataStore
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

class GroupViewModel(private val dataStore: UserDataStore) : ViewModel() {
    private val _uiState = MutableStateFlow(GroupUiState())
    val uiState: StateFlow<GroupUiState> = _uiState.asStateFlow()

    init{loadData()}

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
            _uiState.update { it.copy(isAddingBackground = 1f)}
            _uiState.update { it.copy(isAdding = false)}
        }else{
            _uiState.update { it.copy(isAddingBackground = 0.5f)}
            _uiState.update { it.copy(isAdding = true)}
        }

    }

    fun openInviteWindow(){
        if(uiState.value.isInvite==false){
            _uiState.update { it.copy(isAddingBackground = 0.5f)}
            _uiState.update { it.copy(isInvite = true)}
        }else{
            _uiState.update { it.copy(isAddingBackground = 1f)}
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
            val call: Call<GroupResponse> = ApiClient().addGroup("Bearer " + accessToken, GroupData(uiState.value.newGroupName, uiState.value.newGroupDescription))
            call.enqueue(object : Callback<GroupResponse> {
                override fun onResponse(call: Call<GroupResponse>, response: Response<GroupResponse>) {
                    addingGroup()
                    loadData()
                }
                override fun onFailure(call: Call<GroupResponse>, t: Throwable) {
                    //дописать
                }
            })
        }
    }

    fun loadData() {
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            val call: Call<List<GroupResponse>> = ApiClient().getPersonalGroup("Bearer " + accessToken)
            call.enqueue(object : Callback<List<GroupResponse>> {
                override fun onResponse(call: Call<List<GroupResponse>>, response: Response<List<GroupResponse>>) {
                    val validData: MutableList<GroupResponse> = mutableListOf()
                    response.body()?.forEach { item ->
                        if (item.is_personal ) {
                            validData.add(item)
                        }
                    }
                    _uiState.update { it.copy(personalGroup = validData)}
                    _uiState.update { it.copy(searchPersonalGroup = validData)}
                }

                override fun onFailure(call: Call<List<GroupResponse>>, t: Throwable) {
                    //дописать
                }
            })
            val call1: Call<List<GroupResponse>> = ApiClient().getGroupGroup("Bearer " + accessToken)
            call1.enqueue(object : Callback<List<GroupResponse>> {
                override fun onResponse(call1: Call<List<GroupResponse>>, response: Response<List<GroupResponse>>) {
                    val validData: MutableList<GroupResponse> = mutableListOf()
                    response.body()?.forEach { item ->
                        if (!item.is_personal) {
                            validData.add(item)
                        }
                    }
                    _uiState.update { it.copy(groupGroup = validData)}
                    _uiState.update { it.copy(searchGroupGroup = validData)}

                }

                override fun onFailure(call1: Call<List<GroupResponse>>, t: Throwable) {
                    //дописать
                }
            })
        }
    }

    fun joinByInvite(){
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            val call: Call<MessageData> = ApiClient().joinGroupByInvitation("Bearer " + accessToken, JoinByInviteProjectData(uiState.value.invite))
            call.enqueue(object : Callback<MessageData> {
                override fun onResponse(call: Call<MessageData>, response: Response<MessageData>) {
                    _uiState.update { it.copy(invite_message = response.body()!!.message!!) }
                    loadData()
                }

                override fun onFailure(call: Call<MessageData>, t: Throwable) {
                    //дописать
                }
            })
        }
    }
}