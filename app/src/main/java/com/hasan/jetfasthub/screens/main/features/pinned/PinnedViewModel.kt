package com.hasan.jetfasthub.screens.main.features.pinned

import androidx.lifecycle.ViewModel
import com.hasan.jetfasthub.core.ui.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PinnedViewModel: ViewModel() {
    private var _state : MutableStateFlow<PinnedScreenState> = MutableStateFlow(PinnedScreenState())
    val state = _state.asStateFlow()
}

data class PinnedScreenState(
    val pinnedRepositories: Resource<Int> = Resource.Loading(),
    val pinnedIssues: Resource<Int> = Resource.Loading(),
    val pinnedPullRequests: Resource<Int> = Resource.Loading(),
    val pinnedGists: Resource<Int> = Resource.Loading()
)