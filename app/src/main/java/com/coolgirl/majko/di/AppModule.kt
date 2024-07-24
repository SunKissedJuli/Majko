package com.coolgirl.majko.di

import com.coolgirl.majko.Screen.Archive.ArchiveViewModel
import com.coolgirl.majko.Screen.Group.GroupViewModel
import com.coolgirl.majko.Screen.Login.LoginViewModel
import com.coolgirl.majko.Screen.Profile.ProfileViewModel
import com.coolgirl.majko.Screen.Project.ProjectViewModel
import com.coolgirl.majko.Screen.Task.TaskViewModel
import com.coolgirl.majko.data.MajkoRepository
import com.coolgirl.majko.data.dataStore.UserDataStore
import com.coolgirl.majko.data.remote.ApiMajko
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import javax.inject.Singleton

val dataStoreModule = module {
    single { UserDataStore(androidContext()) }
}

val apiModule = module {
    single { MajkoRepository(ApiClient()) }
}

val appModule = module {
    viewModel { ArchiveViewModel(get()) }
    viewModel { GroupViewModel(get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { ProjectViewModel(get()) }
    viewModel { TaskViewModel(get()) }
}

/*object AppModule{
    @Singleton
    fun provideMajkoRepository(api: ApiMajko): MajkoRepository{
        return MajkoRepository(api)
    }
}*/