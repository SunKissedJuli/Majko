package com.coolgirl.majko.Screen.Profile

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
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
import com.coolgirl.majko.data.remote.ApiError
import com.coolgirl.majko.data.remote.ApiExeption
import com.coolgirl.majko.data.remote.ApiSuccess
import com.coolgirl.majko.data.MajkoUserRepository
import com.coolgirl.majko.data.dataStore.UserDataStore
import com.coolgirl.majko.data.remote.dto.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import com.coolgirl.majko.R
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ProfileViewModel(private val majkoRepository: MajkoUserRepository) : ViewModel(), KoinComponent {
    private val dataStore: UserDataStore by inject()
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
        if(!uiState.value.userName.equals(uiState.value.currentUser!!.name)){
            viewModelScope.launch {
                majkoRepository.updateUserName(UserUpdateName(uiState.value.userName))
                    .collect() { response ->
                        when (response) {
                            is ApiSuccess -> {
                                _uiState.update { it.copy(currentUser = response.data) }
                                isMessage(R.string.message_success)
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
    }

    fun isError(message: Int?){
        if(uiState.value.isError){
            _uiState.update { it.copy(isError = false)}
        }else{
            _uiState.update { it.copy(errorMessage = message)}
            _uiState.update { it.copy(isError = true)}
        }
    }

    fun isMessage(message: Int?){
        if(uiState.value.isMessage){
            _uiState.update { it.copy(isMessage = false)}
        }else{
            _uiState.update { it.copy(message = message)}
            _uiState.update { it.copy(isMessage = true)}
        }
    }

    fun updateEmailData() {
        viewModelScope.launch {
            majkoRepository.updateUserEmail(UserUpdateEmail(uiState.value.userName, uiState.value.userEmail)).collect() { response ->
                when(response){
                    is ApiSuccess ->{
                        _uiState.update { it.copy(currentUser = response.data) }
                        isMessage(R.string.message_success)
                    }
                    is ApiError -> {
                        if(response.code==422){
                            isError(R.string.error_email_notnew)
                        }else{
                            isError(R.string.error_data)
                        }
                        Log.d("TAG", "error message = " + response.message)
                    }
                    is ApiExeption -> {
                        isError(R.string.error_data)
                        Log.d("TAG", "exeption e = " + response.e)}
                    else -> {}
                }
            }
        }
    }

    fun changePasswordScreen(){
        if(uiState.value.isChangePassword){
            _uiState.update { it.copy(isChangePassword = false)}
        }else{
            _uiState.update { it.copy(isChangePassword = true)}
        }
    }

    fun forgetAccount(){
        viewModelScope.launch {
            dataStore.setAccesToken("")
        }
    }

    fun loadData() {
        viewModelScope.launch {
            majkoRepository.currentUser().collect() { response ->
                when(response){
                    is ApiSuccess ->{
                        updateUserEmail(response.data?.email.toString())
                        updateUserName(response.data?.name.toString())
                        _uiState.update { it.copy(currentUser = response.data) }
                }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                    else -> {}
                }
            }
        }
    }

    fun changePassword(){
        viewModelScope.launch {
            majkoRepository.updateUserPassword(UserUpdatePassword(uiState.value.userName,
                uiState.value.newPassword,uiState.value.confirmPassword, uiState.value.oldPassword)).collect() { response ->
                when(response){
                    is ApiSuccess ->{ changePasswordScreen() }
                    is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                    is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                    else -> {}
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
                        majkoRepository.updateUserImage(UserUpdateImage(uiState.value.userName, file)).collect() { response ->
                            when(response){
                                is ApiSuccess ->{  }
                                is ApiError -> { Log.d("TAG", "error message = " + response.message) }
                                is ApiExeption -> { Log.d("TAG", "exeption e = " + response.e) }
                                else -> {}
                            }
                        }
                    }
                }

            }
        }
        return launcher
    }


    private fun copyStreamToFile(inputStream: InputStream, outputFile: File){
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
