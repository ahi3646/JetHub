package com.hasan.jetfasthub.screens.login.presentation.basicAuth

import androidx.lifecycle.ViewModel
import com.hasan.jetfasthub.core.ui.navigation.DefaultNavigationEventDelegate
import com.hasan.jetfasthub.core.ui.navigation.NavigationEventDelegate
import com.hasan.jetfasthub.core.ui.navigation.emitNavigation
import com.hasan.jetfasthub.core.ui.utils.Constants.getAuthorizationUrl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * BasicAuth screen view model
 *
 * @author Anorov Hasan on 30/09/2023
 */

class BasicAuthViewModel : ViewModel(),
    NavigationEventDelegate<BasicAuthScreenNavigation> by DefaultNavigationEventDelegate() {

    private val _uiState = MutableStateFlow(BasicAuthUiState())
    val uiState: StateFlow<BasicAuthUiState> = _uiState.asStateFlow()

    fun handleIntents(intent: BasicAuthClickIntents) {
        when (intent) {
            is BasicAuthClickIntents.OnUsernameChanged -> {
                _uiState.update {
                    it.copy(userName = intent.username)
                }
            }

            is BasicAuthClickIntents.OnPasswordChanged -> {
                _uiState.update {
                    it.copy(password = intent.password)
                }
            }

            is BasicAuthClickIntents.BrowserAuth -> {
                emitNavigation(BasicAuthScreenNavigation.BrowserAuth(getAuthorizationUrl()))
            }

            BasicAuthClickIntents.GoPreviousScreen -> {
                emitNavigation(BasicAuthScreenNavigation.GoPreviousScreen)
            }

            BasicAuthClickIntents.LoginClick -> {
                //currently not available
            }
        }
    }

}

data class BasicAuthUiState(
    val userName: String = "",
    val password: String = "",
    val passwordVisibility: Boolean = false
)