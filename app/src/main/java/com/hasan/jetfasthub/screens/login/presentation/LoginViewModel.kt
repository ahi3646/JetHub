package com.hasan.jetfasthub.screens.login.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasan.jetfasthub.core.ui.navigation.DefaultNavigationEventDelegate
import com.hasan.jetfasthub.core.ui.navigation.NavigationEventDelegate
import com.hasan.jetfasthub.core.ui.navigation.emitNavigation
import com.hasan.jetfasthub.core.ui.utils.Constants.getAuthorizationUrl
import com.hasan.jetfasthub.screens.login.domain.AuthRepository
import com.hasan.jetfasthub.screens.login.presentation.loginPage.LoginChooserClickIntents
import com.hasan.jetfasthub.screens.login.presentation.loginPage.LoginChooserNavigation
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository
) : ViewModel(),
    NavigationEventDelegate<LoginChooserNavigation> by DefaultNavigationEventDelegate() {

    private val _state: MutableStateFlow<LoginScreenState> = MutableStateFlow(LoginScreenState())
    val state = _state.asStateFlow()

    fun handleIntents(intent: LoginChooserClickIntents){
        when(intent){
            LoginChooserClickIntents.BasicAuthentication -> {
                emitNavigation(LoginChooserNavigation.BasicAuth)
            }
            LoginChooserClickIntents.OAuthCLick -> emitNavigation(LoginChooserNavigation.OAuth(getAuthorizationUrl()))
        }
    }

    fun getAccessToken(code: String): Flow<String> = callbackFlow {
        viewModelScope.launch {
            try {
                repository.getAccessToken(code).let { response ->
                    if (response.isSuccessful) {
                        if (response.body()!!.access_token != null) {
                            trySend(response.body()!!.access_token!!)
                        } else {
                            trySend("null")
                        }
                    } else {
                        Log.d(
                            "ahi3646",
                            "getAccessToken else : ${response.errorBody().toString()} "
                        )
                        trySend("null")
                    }
                }
            } catch (e: Exception) {
                Log.d("ahi3646", "getAccessToken else : ${e.message.toString()} ")
                trySend("null")
            }
        }
        awaitClose {
            channel.close()
            Log.d("ahi3646", "getAccessToken: channel closed ")
        }
    }

    fun changeStatus(loadingPageStatus: LoginPageState) {
        _state.update { it.copy(loadingPageStatus = loadingPageStatus) }
    }

    //TODO here maybe use other apis like Either if it matches
    fun getAuthenticatedUser(token: String): Flow<String> = callbackFlow {
        viewModelScope.launch {
            try {
                repository.getAuthenticatedUser(token).let { authenticatedUser ->
                    if (authenticatedUser.isSuccessful) {
                        trySend(authenticatedUser.body()!!.login)
                    } else {
                        trySend("")
                    }
                }
            } catch (e: Exception) {
                trySend("")
            }
        }
        awaitClose {
            channel.close()
            Log.d("ahi3646", "getAuthenticatedUser: callback channel stopped ")
        }
    }
}

data class LoginScreenState(
    val loadingPageStatus: LoginPageState = LoginPageState.Default
)

enum class LoginPageState {
    Default,
    Fetching,
    Error,
    Success
}

