package com.hasan.jetfasthub.screens.main.home

import androidx.lifecycle.ViewModel
import com.hasan.jetfasthub.screens.main.AppScreens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {

    private val _state: MutableStateFlow<HomeScreenState> = MutableStateFlow(HomeScreenState())
    val state = _state.asStateFlow()

    fun onBottomBarItemSelected(appScreens: AppScreens) {
        _state.update {
            it.copy(selectedBottomBarItem = appScreens)
        }
    }

}

data class HomeScreenState(
    val selectedBottomBarItem: AppScreens = AppScreens.Feeds
)
