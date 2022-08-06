package com.jokku.githubrepos.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jokku.githubrepos.repository.GithubRepository
import com.jokku.githubrepos.util.Resource
import com.jokku.githubrepos.vo.Repo
import com.jokku.githubrepos.vo.RepoSearchResult
import kotlinx.coroutines.launch
import retrofit2.Response

class GithubViewModel(
    val githubRepository: GithubRepository
) : ViewModel() {

    val orgRepos: MutableLiveData<Resource<List<Repo>>> = MutableLiveData()
    var orgReposPage = 1

    fun searchOrgRepos(query: String, pageNumber: Int) = viewModelScope.launch {
        orgRepos.postValue(Resource.Loading())
        val response = githubRepository.searchOrgRepos(query, pageNumber)
        orgRepos.postValue(handleOrgReposResponse(response))
    }

    private fun handleOrgReposResponse(response: Response<List<Repo>>) : Resource<List<Repo>> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }
}