package com.hasan.jetfasthub.screens.main.repository

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RepositoryViewModel: ViewModel() {

    private var _state: MutableStateFlow<RepositoryScreenState> = MutableStateFlow(
        RepositoryScreenState()
    )
    val state = _state.asStateFlow()

    fun onBottomBarItemClicked(repositoryScreen: RepositoryScreens){
        _state.update {
            it.copy(selectedBottomBarItem = repositoryScreen)
        }
    }

}

data class RepositoryScreenState(
    val selectedBottomBarItem: RepositoryScreens = RepositoryScreens.Code
)

interface RepositoryScreens {

    object Code : RepositoryScreens

    object Issues : RepositoryScreens

    object PullRequest : RepositoryScreens

    object Projects : RepositoryScreens

}