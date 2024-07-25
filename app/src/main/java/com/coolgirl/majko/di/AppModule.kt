package com.coolgirl.majko.di

import com.coolgirl.majko.Sc.GroupEditorScreen
import com.coolgirl.majko.Screen.Archive.ArchiveViewModel
import com.coolgirl.majko.Screen.Group.GroupViewModel
import com.coolgirl.majko.Screen.GroupEditor.GroupEditorViewModel
import com.coolgirl.majko.Screen.Login.LoginViewModel
import com.coolgirl.majko.Screen.Profile.ProfileViewModel
import com.coolgirl.majko.Screen.Project.ProjectViewModel
import com.coolgirl.majko.Screen.ProjectEdit.ProjectEditViewModel
import com.coolgirl.majko.Screen.Task.TaskViewModel
import com.coolgirl.majko.Screen.TaskEditor.TaskEditorViewModel
import com.coolgirl.majko.data.MajkoRepository
import com.coolgirl.majko.data.dataStore.UserDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataStoreModule = module {
    single { UserDataStore(androidContext()) }
}

val apiModule = module {
    single { MajkoRepository(ApiClient()) }
}

val appModule = module {
    viewModel { ArchiveViewModel(get(), get()) }
    viewModel { GroupViewModel(get(), get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { ProjectViewModel(get(), get()) }
    viewModel { TaskViewModel(get(), get()) }
    viewModel { (task_id: String) -> TaskEditorViewModel(get(), get(), task_id) }
    viewModel { (project_id: String) -> ProjectEditViewModel(get(), get(), project_id) }
    viewModel { (group_id: String) -> GroupEditorViewModel(get(), get(), group_id) }
}
