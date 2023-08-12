package com.hasan.jetfasthub.screens.main.issue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hasan.jetfasthub.data.IssueRepository
import com.hasan.jetfasthub.screens.main.issue.issue_model.IssueModel
import com.hasan.jetfasthub.utility.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Exception

class IssueViewModel(private val repository: IssueRepository): ViewModel() {

    private var _state: MutableStateFlow<IssueScreenState> = MutableStateFlow(IssueScreenState())
    val state = _state.asStateFlow()

    fun getIssue(token: String, owner: String, repo: String, issueNumber: String){
        viewModelScope.launch {
            try {
                repository.getIssue(token, owner, repo, issueNumber).let {response ->
                    if(response.isSuccessful){
                        _state.update {
                            it.copy(Issue = Resource.Success(response.body()!!))
                        }
                    }else{
                        _state.update {
                            it.copy(Issue = Resource.Failure(response.errorBody().toString()))
                        }
                    }
                }
            }catch (e: Exception){
                _state.update {
                    it.copy(Issue = Resource.Failure(e.message.toString()))
                }
            }
        }
    }

}

data class IssueScreenState(
    val Issue: Resource<IssueModel> = Resource.Loading()
)

sealed interface IssueScreenSheets{
    object IssueInfoSheet: IssueScreenSheets

    class UnlockIssueSheet(val issueNumber: String): IssueScreenSheets
}
