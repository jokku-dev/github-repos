package com.jokku.githubrepos.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jokku.githubrepos.repository.GithubRepository

class GithubViewModelProviderFactory(
    val githubRepository: GithubRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GithubViewModel(githubRepository) as T
    }
}