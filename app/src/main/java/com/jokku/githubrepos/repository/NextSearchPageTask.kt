package com.jokku.githubrepos.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jokku.githubrepos.api.*
import com.jokku.githubrepos.db.GithubDatabase
import com.jokku.githubrepos.util.Resource
import com.jokku.githubrepos.vo.RepoSearchResult
import java.io.IOException

class NextSearchPageTask constructor(
    private val query: String,
    private val githubService: GithubService,
    private val db: GithubDatabase
) : Runnable {
    private val _liveData = MutableLiveData<Resource<Boolean>>()
    val liveData: LiveData<Resource<Boolean>> = _liveData

    override fun run() {
        val current = db.repoDao().findSearchResult(query)
        if (current == null) {
            _liveData.postValue(Resource.Error("No search Result", null))
            return
        }
        val nextPage = current.next
        if (nextPage == null) {
            _liveData.postValue(Resource.Success(false))
            return
        }
        val newValue = try {
            val response = githubService.searchOrgRepos(query, nextPage).execute()
            when (val apiResponse = ApiResponse.create(response)) {
                is ApiSuccessResponse -> {
                    // we merge all repo ids into 1 list so that it is easier to fetch the
                    // result list.
                    val ids = arrayListOf<Int>()
                    ids.addAll(current.repoIds)

                    ids.addAll(apiResponse.body.items.map { it.id })
                    val merged = RepoSearchResult(
                        query, ids,
                        apiResponse.body.total, apiResponse.nextPage
                    )
                    db.runInTransaction {
                        db.repoDao().insert(merged)
                        db.repoDao().insertRepos(apiResponse.body.items)
                    }
                    Resource.Success(apiResponse.nextPage != null)
                }
                is ApiEmptyResponse -> {
                    Resource.Success(false)
                }
                is ApiErrorResponse -> {
                    Resource.Error(apiResponse.errorMessage, true)
                }
            }

        } catch (e: IOException) {
            Resource.Error(e.message!!, true)
        }
        _liveData.postValue(newValue)
    }
}