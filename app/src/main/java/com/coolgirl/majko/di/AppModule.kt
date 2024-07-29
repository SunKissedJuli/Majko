package com.coolgirl.majko.di

import com.coolgirl.majko.Screen.Archive.ArchiveViewModel
import com.coolgirl.majko.Screen.Group.GroupViewModel
import com.coolgirl.majko.Screen.GroupEditor.GroupEditorViewModel
import com.coolgirl.majko.Screen.Login.LoginViewModel
import com.coolgirl.majko.Screen.Profile.ProfileViewModel
import com.coolgirl.majko.Screen.Project.ProjectViewModel
import com.coolgirl.majko.Screen.ProjectEdit.ProjectEditViewModel
import com.coolgirl.majko.Screen.Register.RegisterViewModel
import com.coolgirl.majko.Screen.Task.TaskViewModel
import com.coolgirl.majko.Screen.TaskEditor.TaskEditorViewModel
import com.coolgirl.majko.data.MajkoUserRepository
import com.coolgirl.majko.data.dataStore.UserDataStore
import com.coolgirl.majko.data.repository.MajkoGroupRepository
import com.coolgirl.majko.data.repository.MajkoInfoRepository
import com.coolgirl.majko.data.repository.MajkoProjectRepository
import com.coolgirl.majko.data.repository.MajkoTaskRepository
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataStoreModule = module {
    single { UserDataStore(androidContext()) }
}

val apiModule = module {
    single { ApiClient(androidContext()) }
    single { MajkoUserRepository(get()) }
    single { MajkoTaskRepository(ApiClient(get())) }
    single { MajkoInfoRepository(ApiClient(get())) }
    single { MajkoProjectRepository(ApiClient(get())) }
    single { MajkoGroupRepository(ApiClient(get())) }
}

val appModule = module {
    viewModel { ArchiveViewModel(get()) }
    viewModel { GroupViewModel(get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { RegisterViewModel(get(), get()) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { ProjectViewModel(get()) }
    viewModel { TaskViewModel(get(), get()) }
    viewModel { TaskEditorViewModel(get(), get())}
    viewModel { ProjectEditViewModel(get(),get(), get()) }
    viewModel { GroupEditorViewModel(get(), get()) }
}
