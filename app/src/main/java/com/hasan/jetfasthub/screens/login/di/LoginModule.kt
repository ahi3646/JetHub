package com.hasan.jetfasthub.screens.login.di

import com.hasan.jetfasthub.data.PreferenceHelper
import com.hasan.jetfasthub.screens.login.data.AuthRepositoryImpl
import com.hasan.jetfasthub.screens.login.domain.AuthRepository
import com.hasan.jetfasthub.screens.login.domain.AuthUseCase
import com.hasan.jetfasthub.screens.login.presentation.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val loginModule = module {
    single { PreferenceHelper(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single { AuthUseCase(get()) }
    viewModel { LoginViewModel(get()) }
}