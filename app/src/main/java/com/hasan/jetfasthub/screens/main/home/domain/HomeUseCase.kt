package com.hasan.jetfasthub.screens.main.home.domain

import arrow.core.Either
import arrow.core.raise.catch
import arrow.core.raise.either
import com.hasan.jetfasthub.screens.main.home.data.models.user_model.GitHubUser
import com.hasan.jetfasthub.screens.main.search.models.issues_model.IssuesModel

class HomeUseCase(private val repository: HomeRepository) {

    suspend fun getAuthenticatedUserData(): Either<NetworkErrors, GitHubUser> {
        return either {
            catch(
                block = { repository.getUser() },
                catch = { raise(NetworkErrors.NetworkFail(it)) }
            )
        }
    }

    fun getAuthenticatedUsername(): String = repository.getAuthenticatedUsername()

    suspend fun getIssuesWithCount(query: String, page: Int): Either<NetworkErrors, IssuesModel> {
        return either {
            catch(
                block = { repository.getIssuesWithCount(query, page) },
                catch = { raise(NetworkErrors.NetworkFail(it)) }
            )
        }
    }

    suspend fun getPullsWithCount(query: String, page: Int): Either<NetworkErrors, IssuesModel> {
        return either {
            catch(
                block = { repository.getPullsWithCount(query, page) },
                catch = { raise(NetworkErrors.NetworkFail(it)) }
            )
        }
    }

}

sealed class NetworkErrors {
    data class NetworkFail(val cause: Throwable) : NetworkErrors()
}