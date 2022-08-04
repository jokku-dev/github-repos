package com.jokku.githubrepos.repository

import com.jokku.githubrepos.api.RetrofitInstance
import com.jokku.githubrepos.db.GithubDatabase

class GithubRepository(
    val db: GithubDatabase
) {

    suspend fun searchOrgRepos(query: String, pageNumber: Int) =
        RetrofitInstance.api.searchOrgRepos(query, pageNumber)
}