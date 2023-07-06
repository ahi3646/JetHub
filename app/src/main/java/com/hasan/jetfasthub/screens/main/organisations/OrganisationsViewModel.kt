package com.hasan.jetfasthub.screens.main.organisations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasan.jetfasthub.data.OrganisationRepository
import com.hasan.jetfasthub.screens.main.organisations.model.OrganisationMemberModel
import com.hasan.jetfasthub.utility.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrganisationsViewModel(private val repository: OrganisationRepository) : ViewModel() {

    private var _state: MutableStateFlow<OrganisationScreenState> = MutableStateFlow(
        OrganisationScreenState()
    )
    val state = _state.asStateFlow()

    fun getOrganisationMembers(token: String, organisation: String, page: Int) {
        viewModelScope.launch {
            try {
                repository.getOrganisationMembers(token, organisation, page)
                    .let { organisationMembers ->
                        if (organisationMembers.isSuccessful) {
                            _state.update {
                                it.copy(OrganisationMembers = Resource.Success(organisationMembers.body()!!))
                            }
                        } else {
                            _state.update {
                                it.copy(
                                    OrganisationMembers = Resource.Failure(
                                        organisationMembers.errorBody().toString()
                                    )
                                )
                            }
                        }
                    }
            } catch (e: Exception) {
                _state.update {
                    it.copy(OrganisationMembers = Resource.Failure(e.message.toString()))
                }
            }
        }
    }




}

data class OrganisationScreenState(
    val OrganisationMembers: Resource<OrganisationMemberModel> = Resource.Loading()
)