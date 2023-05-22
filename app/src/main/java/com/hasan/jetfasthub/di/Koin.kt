package com.hasan.jetfasthub.di

import com.hasan.jetfasthub.data.AuthRepository
import com.hasan.jetfasthub.data.AuthRepositoryImpl
import com.hasan.jetfasthub.screens.login.LoginViewModel
import com.hasan.jetfasthub.screens.login.basic_auth.BasicAuthViewModel
import com.hasan.jetfasthub.data.HomeRepository
import com.hasan.jetfasthub.data.HomeRepositoryImpl
import com.hasan.jetfasthub.data.ProfileRepository
import com.hasan.jetfasthub.data.ProfileRepositoryImpl
import com.hasan.jetfasthub.screens.main.home.HomeViewModel
import com.hasan.jetfasthub.screens.main.profile.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    viewModel { LoginViewModel(get()) }
}

val profileModule = module {
    single <ProfileRepository> { ProfileRepositoryImpl(get()) }
    viewModel{ProfileViewModel(get())}
}

val eventsModule = module {
    single<HomeRepository> { HomeRepositoryImpl(get()) }
    viewModel { HomeViewModel(get()) }
}

val basicAuthViewModelModule = module {
    viewModel { BasicAuthViewModel() }
}

val homeViewModelModule = module {
    viewModelOf(::HomeViewModel)
}