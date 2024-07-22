package com.coolgirl.majko.Screen.Profile

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.coolgirl.majko.data.dataStore.UserDataStore
import com.coolgirl.majko.data.remote.dto.*
import com.coolgirl.majko.di.ApiClient
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class ProfileViewModel(private val dataStore: UserDataStore) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init { loadData() }

    fun updateUserName(username: String) {
        _uiState.update { it.copy(userName = username) }
    }

    fun updateUserEmail(email: String) {
        _uiState.update { it.copy(userEmail = email) }
    }

    fun updateOldPassword(password: String) {
        _uiState.update { it.copy(oldPassword = password) }
    }

    fun updateNewPassword(password: String) {
        _uiState.update { it.copy(newPassword = password) }
    }

    fun updateConfirmPassword(password: String) {
        _uiState.update { it.copy(confirmPassword = password) }
    }

    fun updateNameData() {
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            val call: Call<CurrentUserDataResponse> = ApiClient().updateUserName("Bearer " + accessToken, UserUpdateName(uiState.value.userName))
            call.enqueue(object : Callback<CurrentUserDataResponse> {
                override fun onResponse(call: Call<CurrentUserDataResponse>, response: Response<CurrentUserDataResponse>) {
                    if(response.code()==200){
                    }
                }
                override fun onFailure(call: Call<CurrentUserDataResponse>, t: Throwable) {
                    //дописать
                } })
        }
    }

    fun updateEmailData() {
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            val call: Call<CurrentUserDataResponse> = ApiClient().updateUserEmail("Bearer " + accessToken, UserUpdateEmail(uiState.value.userName, uiState.value.userEmail))
            call.enqueue(object : Callback<CurrentUserDataResponse> {
                override fun onResponse(call: Call<CurrentUserDataResponse>, response: Response<CurrentUserDataResponse>) {
                    if(response.code()==200){
                    }
                }
                override fun onFailure(call: Call<CurrentUserDataResponse>, t: Throwable) {
                    //дописать
                } })
        }
    }

    fun changePasswordScreen(){
        if(uiState.value.is_adding){
            _uiState.update { it.copy(is_adding_background = 1f)}
            _uiState.update { it.copy(is_adding = false)}
        }else{
            _uiState.update { it.copy(is_adding_background = 0.5f)}
            _uiState.update { it.copy(is_adding = true)}
        }

    }

    fun forgetAccount(){
        viewModelScope.launch {
            dataStore.setAccesToken("")
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            val call: Call<CurrentUserDataResponse> = ApiClient().currentUser("Bearer " + accessToken)
            call.enqueue(object : Callback<CurrentUserDataResponse> {
                override fun onResponse(call: Call<CurrentUserDataResponse>, response: Response<CurrentUserDataResponse>) {
                    if(response.code()==200){
                        updateUserEmail(response.body()?.email.toString())
                        updateUserName(response.body()?.name.toString())
                    }
                }
                override fun onFailure(call: Call<CurrentUserDataResponse>, t: Throwable) {
                    //дописать
                } })
        }
    }

    fun changePassword(){
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            val call: Call<CurrentUserDataResponse> = ApiClient().updateUserPassword("Bearer " + accessToken, UserUpdatePassword(uiState.value.userName,
                uiState.value.newPassword,uiState.value.confirmPassword, uiState.value.oldPassword))
            call.enqueue(object : Callback<CurrentUserDataResponse> {
                override fun onResponse(call: Call<CurrentUserDataResponse>, response: Response<CurrentUserDataResponse>) {
                    if(response.code()==200){
                        changePasswordScreen()
                    }
                }
                override fun onFailure(call: Call<CurrentUserDataResponse>, t: Throwable) {
                    //дописать
                } })
        }
    }

    @SuppressLint("Range", "SuspiciousIndentation")
    @Composable
    fun OpenGalery(context: Context = LocalContext.current): ManagedActivityResultLauncher<String, Uri?> {
        var file by remember { mutableStateOf<File?>(null) }
        val coroutineScope = rememberCoroutineScope()
        val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            val cursor: Cursor? = context.getContentResolver().query(uri!!, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    var fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    val iStream : InputStream = context.contentResolver.openInputStream(uri!!)!!
                    val outputDir : File = context.cacheDir
                    val outputFile : File = File(outputDir,fileName)
                    copyStreamToFile(iStream, outputFile)
                    file = outputFile
                    iStream.close()

                }
            }finally { cursor!!.close()
                coroutineScope.launch() {
                    viewModelScope.launch {
                        val accessToken = dataStore.getAccessToken().first() ?: ""
                        val call: Call<CurrentUserDataResponse> = ApiClient().updateUserImage("Bearer " + accessToken, UserUpdateImage(uiState.value.userName, file))
                        call.enqueue(object : Callback<CurrentUserDataResponse> {
                            override fun onResponse(call: Call<CurrentUserDataResponse>, response: Response<CurrentUserDataResponse>) {
                                if(response.code()==200){
                                }
                            }
                            override fun onFailure(call: Call<CurrentUserDataResponse>, t: Throwable) {
                                //дописать
                            } })
                    }
                }

            }
        }
        return launcher
    }


    fun copyStreamToFile(inputStream: InputStream, outputFile: File){
        inputStream.use { input ->
            val outputStream = FileOutputStream(outputFile)
            outputStream.use { output ->
                val buffer = ByteArray(4 * 1024) // buffer size
                while (true) {
                    val byteCount = input.read(buffer)
                    if (byteCount < 0) break
                    output.write(buffer, 0, byteCount)
                }
               output.flush()
            }
        }
    }
}
