package com.hasan.jetfasthub.screens.main.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasan.jetfasthub.data.NotificationRepository
import com.hasan.jetfasthub.screens.main.notifications.model.Notification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotificationsViewModel(private val repository: NotificationRepository) : ViewModel() {

    private var _state: MutableStateFlow<NotificationsScreenState> = MutableStateFlow(
        NotificationsScreenState()
    )
    val state = _state.asStateFlow()

    fun getAllNotifications(token: String){
        viewModelScope.launch {
            repository.getAllNotifications(token).let { allNotifications ->
                if (allNotifications.isSuccessful){
                    _state.update {
                        it.copy(allNotifications = AllNotifications.Success(allNotifications.body()!!))
                    }
                }else{
                    _state.update {
                        it.copy(allNotifications = AllNotifications.Failure(allNotifications.errorBody().toString()))
                    }
                }
            }
        }
    }
}


data class NotificationsScreenState(
    val allNotifications: AllNotifications = AllNotifications.Loading
)


sealed interface AllNotifications {
    object Loading : AllNotifications
    data class Success(val notification: Notification) : AllNotifications
    data class Failure(val errorMessage: String) : AllNotifications
}