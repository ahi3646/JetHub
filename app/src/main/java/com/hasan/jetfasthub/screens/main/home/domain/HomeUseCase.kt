package com.hasan.jetfasthub.screens.main.home.domain

import com.hasan.jetfasthub.data.HomeRepository

class HomeUseCase(
    private val repository: HomeRepository
) {

    suspend fun getUser(token: String, username: String) {
        repository.getUser(token, username)
    }

    suspend fun getIssuesWithCount(token: String, query: String, page: Int){
        repository.getIssuesWithCount(token, query, page)
    }

    suspend fun getPullsWithCount(token: String, query: String, page: Int) {
        repository.getPullsWithCount(token, query, page)
    }

}