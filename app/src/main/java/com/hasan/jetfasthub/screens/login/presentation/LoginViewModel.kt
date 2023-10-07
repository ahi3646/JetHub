package com.hasan.jetfasthub.screens.login.presentation

import android.content.Intent
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasan.jetfasthub.R
import com.hasan.jetfasthub.core.ui.extensions.TextReference
import com.hasan.jetfasthub.core.ui.extensions.resourceReference
import com.hasan.jetfasthub.core.ui.navigation.DefaultNavigationEventDelegate
import com.hasan.jetfasthub.core.ui.navigation.NavigationEventDelegate
import com.hasan.jetfasthub.core.ui.navigation.emitNavigation
import com.hasan.jetfasthub.core.ui.utils.Constants
import com.hasan.jetfasthub.core.ui.utils.Constants.getAuthorizationUrl
import com.hasan.jetfasthub.screens.login.domain.AuthUseCase
import com.hasan.jetfasthub.screens.login.presentation.loginPage.LoginChooserClickIntents
import com.hasan.jetfasthub.screens.login.presentation.loginPage.LoginChooserNavigation
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * LoginChooser and LoginActivity  view model
 *
 * @author Anorov Hasan on 30/09/2023
 */

class LoginViewModel(
    private val authUseCase: AuthUseCase
) : ViewModel(), DefaultLifecycleObserver,
    NavigationEventDelegate<LoginChooserNavigation> by DefaultNavigationEventDelegate() {

    private val _state: MutableStateFlow<LoginScreenState> = MutableStateFlow(LoginScreenState())
    val state = _state.asStateFlow()

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        if (authUseCase.isLogged()) {
            emitNavigation(LoginChooserNavigation.NavigateToMain)
        }
    }

    fun handleIntents(intent: LoginChooserClickIntents) {
        when (intent) {
            LoginChooserClickIntents.BasicAuthentication -> {
                emitNavigation(LoginChooserNavigation.BasicAuth)
            }
            LoginChooserClickIntents.OAuthCLick -> emitNavigation(
                LoginChooserNavigation.OAuth(
                    getAuthorizationUrl()
                )
            )
        }
    }

    fun handleAuthIntent(intent: Intent?) {
        if (intent != null && intent.data != null) {
            val uri = intent.data
            if (uri.toString().startsWith(Constants.REDIRECT_URL)) {
                val tokenCode = uri!!.getQueryParameter("code")
                if (tokenCode!!.isNotBlank()) {
                    getAccessToken(tokenCode)
                } else {
                    updateLoginStatus(LoginPageState.Error(resourceReference(R.string.login_token_code_not_found)))
                }
            }
        }
    }

    private fun getAccessToken(code: String) {
        updateLoginStatus(LoginPageState.Fetching)
        viewModelScope.launch(
            CoroutineExceptionHandler { _, _ ->
                updateLoginStatus(LoginPageState.Error(resourceReference(R.string.login_status_error)))
            }
        ) {
            try {
                authUseCase.getAccessToken(code).let { response ->
                    if (response.accessToken.isNotEmpty()) {
                        authUseCase.saveToken(token = response.accessToken)
                        getAuthenticatedUser(token = response.accessToken)
                    } else {
                        updateLoginStatus(LoginPageState.Error(resourceReference(R.string.login_invalid_token)))
                    }
                }
            } catch (e: Exception) {
                updateLoginStatus(LoginPageState.Error(resourceReference(R.string.login_network_error)))
            }
        }
    }

    private fun getAuthenticatedUser(token: String) {
        viewModelScope.launch(
            CoroutineExceptionHandler { _, _ ->
                updateLoginStatus(LoginPageState.Error(resourceReference(R.string.login_status_error)))
            }
        ) {
            try {
                authUseCase.getAuthenticatedUser(token).let { authenticatedUser ->
                    if (authenticatedUser.isSuccessful) {
                        authUseCase.saveAuthenticatedUser(authenticatedUser.body()!!.login)
                        updateLoginStatus(LoginPageState.Success)
                        emitNavigation(LoginChooserNavigation.NavigateToMain)
                    } else {
                        updateLoginStatus(LoginPageState.Error(resourceReference(R.string.login_unsuccessful_response)))
                    }
                }
            } catch (e: Exception) {
                updateLoginStatus(LoginPageState.Error(resourceReference(R.string.login_network_error)))
            }
        }
    }

    private fun updateLoginStatus(loadingPageStatus: LoginPageState) {
        _state.update { it.copy(loadingPageStatus = loadingPageStatus) }
    }

}

data class LoginScreenState(
    val loadingPageStatus: LoginPageState = LoginPageState.Default
)

sealed interface LoginPageState {
    data object Default : LoginPageState
    data object Fetching : LoginPageState
    data class Error(val errorMessage: TextReference) : LoginPageState
    data object Success : LoginPageState
}

