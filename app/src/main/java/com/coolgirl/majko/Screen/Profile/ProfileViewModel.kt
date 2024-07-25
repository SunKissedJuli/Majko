package com.coolgirl.majko.Screen.Profile

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.coolgirl.majko.commons.ApiError
import com.coolgirl.majko.commons.ApiExeption
import com.coolgirl.majko.commons.ApiSuccess
import com.coolgirl.majko.data.MajkoRepository
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

class ProfileViewModel(private val dataStore: UserDataStore, private val majkoRepository: MajkoRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

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
            majkoRepository.updateUserName("Bearer " + accessToken,  UserUpdateName(uiState.value.userName)).collect() { response ->
                when(response){
                    is ApiSuccess ->{}
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

    fun updateEmailData() {
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            majkoRepository.updateUserEmail("Bearer " + accessToken,  UserUpdateEmail(uiState.value.userName, uiState.value.userEmail)).collect() { response ->
                when(response){
                    is ApiSuccess ->{}
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

    fun changePasswordScreen(){
        if(uiState.value.isAdding){
            _uiState.update { it.copy(isAddingBackground = 1f)}
            _uiState.update { it.copy(isAdding = false)}
        }else{
            _uiState.update { it.copy(isAddingBackground = 0.5f)}
            _uiState.update { it.copy(isAdding = true)}
        }

    }

    fun forgetAccount(){
        viewModelScope.launch {
            dataStore.setAccesToken("")
        }
    }

    fun loadData() {
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            majkoRepository.currentUser("Bearer " + accessToken).collect() { response ->
                when(response){
                    is ApiSuccess ->{
                        updateUserEmail(response.data?.email.toString())
                        updateUserName(response.data?.name.toString())
                }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
        }
    }

    fun changePassword(){
        viewModelScope.launch {
            val accessToken = dataStore.getAccessToken().first() ?: ""
            majkoRepository.updateUserPassword("Bearer " + accessToken,  UserUpdatePassword(uiState.value.userName,
                uiState.value.newPassword,uiState.value.confirmPassword, uiState.value.oldPassword)).collect() { response ->
                when(response){
                    is ApiSuccess ->{ changePasswordScreen() }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                }
            }
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
                        majkoRepository.updateUserImage("Bearer " + accessToken,   UserUpdateImage(uiState.value.userName, file)).collect() { response ->
                            when(response){
                                is ApiSuccess ->{  }
                                is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                                is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                            }
                        }
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
