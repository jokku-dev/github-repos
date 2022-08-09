package com.jokku.githubrepos.repository

import androidx.lifecycle.LiveData
import com.jokku.githubrepos.api.GithubService
import com.jokku.githubrepos.api.RetrofitInstance
import com.jokku.githubrepos.db.GithubDatabase
import com.jokku.githubrepos.db.RepoDao
import com.jokku.githubrepos.util.Resource
import java.util.concurrent.Executors

class GithubRepository(
    private val db: GithubDatabase,
    private val repoDao: RepoDao,
    private val githubService: GithubService
) {

    suspend fun searchOrgRepos(query: String, pageNumber: Int) =
        RetrofitInstance.api.searchOrgRepos(query, pageNumber)

    fun searchNextPage(query: String): LiveData<Resource<Boolean>> {
        val fetchNextSearchPageTask = NextSearchPageTask(
            query = query,
            githubService = githubService,
            db = db
        )
        Executors.newFixedThreadPool(3).execute(fetchNextSearchPageTask)
        return fetchNextSearchPageTask.liveData
    }
}