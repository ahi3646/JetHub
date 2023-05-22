package com.hasan.jetfasthub.di

import com.hasan.jetfasthub.data.AuthRepository
import com.hasan.jetfasthub.data.AuthRepositoryImpl
import com.hasan.jetfasthub.screens.login.LoginViewModel
import com.hasan.jetfasthub.screens.login.basic_auth.BasicAuthViewModel
import com.hasan.jetfasthub.screens.main.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    viewModel { LoginViewModel(get()) }
}

val basicAuthViewModelModule = module {
    viewModel { BasicAuthViewModel() }
}

val homeViewModelModule = module {
    viewModel {
        HomeViewModel()
    }
}