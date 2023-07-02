package com.hasan.jetfasthub.screens.main.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasan.jetfasthub.data.ProfileRepository
import com.hasan.jetfasthub.screens.main.home.user_model.GitHubUser
import com.hasan.jetfasthub.screens.main.profile.model.event_model.UserEvents
import com.hasan.jetfasthub.screens.main.profile.model.org_model.OrgModel
import com.hasan.jetfasthub.screens.main.profile.model.repo_model.UserRepositoryModel
import com.hasan.jetfasthub.utility.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Exception

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {

    private var _state: MutableStateFlow<ProfileScreenState> =
        MutableStateFlow(ProfileScreenState())
    val state = _state.asStateFlow()

    fun getUser(token: String, username: String) {
        viewModelScope.launch {
            repository.getUser(token, username).let { user ->
                if (user.isSuccessful) {
                    _state.update {
                        it.copy(OverviewScreenState = UserOverviewScreen.Content(user.body()!!))
                    }
                } else {
                    _state.update {
                        it.copy(
                            OverviewScreenState = UserOverviewScreen.Error(
                                user.errorBody().toString()
                            )
                        )
                    }
                }
            }
        }
    }

    fun getUserOrganisations(token: String, username: String) {
        viewModelScope.launch {
            repository.getUserOrgs(token, username).let { organisations ->
                if (organisations.isSuccessful) {
                    _state.update {
                        it.copy(Organisations = Resource.Success(organisations.body()!!))
                    }
                } else {
                    _state.update {
                        it.copy(
                            Organisations = Resource.Failure(
                                organisations.errorBody().toString()
                            )
                        )
                    }
                }
            }
        }
    }

    fun getUserEvents(token: String, username: String) {
        viewModelScope.launch {
            try {
                repository.getUserEvents(token, username).let { userEvents ->
                    if (userEvents.isSuccessful) {
                        _state.update {
                            it.copy(
                                UserEvents = Resource.Success(userEvents.body()!!)
                            )
                        }
                    } else {
                        _state.update {
                            it.copy(
                                UserEvents = Resource.Failure(userEvents.errorBody()!!.toString())
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        UserEvents = Resource.Failure(e.message.toString())
                    )
                }
                Log.d("ahi3646", "getUserEvents: ${e.message} ")
            }
        }
    }

    fun getUserRepositories(token: String, username: String) {
        viewModelScope.launch {
            try {
                repository.getUserRepository(token, username).let { userRepositories ->
                    if (userRepositories.isSuccessful) {
                        _state.update {
                            it.copy(
                                UserRepositories = Resource.Success(userRepositories.body()!!)
                            )
                        }
                    } else {
                        _state.update {
                            it.copy(
                                UserRepositories = Resource.Failure(
                                    userRepositories.errorBody().toString()
                                )
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        UserRepositories = Resource.Failure(e.message.toString())
                    )
                }
            }
        }
    }

}

data class ProfileScreenState(
    val OverviewScreenState: UserOverviewScreen = UserOverviewScreen.Loading,
    val Organisations: Resource<OrgModel> = Resource.Loading(),
    val UserEvents: Resource<UserEvents> = Resource.Loading(),
    val UserRepositories: Resource<UserRepositoryModel> = Resource.Loading()
)

sealed interface UserOverviewScreen {

    object Loading : UserOverviewScreen
    data class Content(val user: GitHubUser) : UserOverviewScreen
    data class Error(val message: String) : UserOverviewScreen

}
