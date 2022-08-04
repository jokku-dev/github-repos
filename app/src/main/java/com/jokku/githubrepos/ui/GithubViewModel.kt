package com.jokku.githubrepos.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jokku.githubrepos.repository.GithubRepository
import com.jokku.githubrepos.util.Resource
import com.jokku.githubrepos.vo.RepoSearchResult

class GithubViewModel(
    val githubRepository: GithubRepository
) : ViewModel() {

    val orgRepos: MutableLiveData<Resource<RepoSearchResult>> = MutableLiveData()
    var orgReposPage = 1
}