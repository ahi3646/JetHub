package com.hasan.jetfasthub.screens.main.home.domain

import arrow.core.Either
import arrow.core.raise.either
import com.hasan.jetfasthub.screens.main.home.data.models.user_model.GitHubUser
import com.hasan.jetfasthub.screens.main.search.models.issues_model.IssuesModel

class HomeUseCase(private val repository: HomeRepository) {

    suspend fun getAuthenticatedUserData():Either<Exception, GitHubUser>{
        return either {
            repository.getUser()
        }
    }

    suspend fun getIssuesWithCount(query: String, page: Int): Either<Exception, IssuesModel> {
        return either {
            repository.getIssuesWithCount(query, page)
        }
    }

    suspend fun getPullsWithCount( query: String, page: Int): Either<Exception, IssuesModel> {
        return either {
            repository.getPullsWithCount(query, page)
        }
    }

}