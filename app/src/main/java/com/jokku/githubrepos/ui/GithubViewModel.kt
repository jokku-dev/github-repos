package com.jokku.githubrepos.ui

import androidx.lifecycle.*
import com.jokku.githubrepos.repository.GithubRepository
import com.jokku.githubrepos.util.Resource
import com.jokku.githubrepos.vo.Repo
import com.jokku.githubrepos.vo.RepoSearchResult
import kotlinx.coroutines.launch
import retrofit2.Response

class GithubViewModel(
    val githubRepository: GithubRepository
) : ViewModel() {

    val orgRepos = MutableLiveData<Resource<List<Repo>>>()
    val query = MutableLiveData<String>()
    var orgReposPage = 1
    private val nextPageHandler = NextPageHandler(githubRepository)

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
        return Resource.Error(response.message(), response.body())
    }

    fun loadNextPage() {
        query.value?.let {
            if (it.isNotBlank()) {
                nextPageHandler.queryNextPage(it)
            }
        }
    }

    class LoadMoreState(val isRunning: Boolean, val errorMessage: String?) {
        private var handledError = false

        val errorMessageIfNotHandled: String?
            get() {
                if (handledError) {
                    return null
                }
                handledError = true
                return errorMessage
            }
    }

    class NextPageHandler(private val repository: GithubRepository) : Observer<Resource<Boolean>> {
        private var nextPageLiveData: LiveData<Resource<Boolean>>? = null
        val loadMoreState = MutableLiveData<LoadMoreState>()
        private var query: String? = null
        private var _hasMore: Boolean = false
        val hasMore
            get() = _hasMore

        init {
            reset()
        }

        fun queryNextPage(query: String) {
            if (this.query == query) {
                return
            }
            unregister()
            this.query = query
            nextPageLiveData = repository.searchNextPage(query)
            loadMoreState.value = LoadMoreState(
                isRunning = true,
                errorMessage = null
            )
            nextPageLiveData?.observeForever(this)
        }

        override fun onChanged(result: Resource<Boolean>?) {
            if (result == null) {
                reset()
            } else {
                when (result) {
                    is Resource.Success -> {
                        _hasMore = result.data == true
                        unregister()
                        loadMoreState.setValue(
                            LoadMoreState(
                                isRunning = false,
                                errorMessage = null
                            )
                        )
                    }
                    is Resource.Error -> {
                        _hasMore = true
                        unregister()
                        loadMoreState.setValue(
                            LoadMoreState(
                                isRunning = false,
                                errorMessage = result.message
                            )
                        )
                    }
                    is Resource.Loading -> {
                        // ignore
                    }
                }
            }
        }

        private fun unregister() {
            nextPageLiveData?.removeObserver(this)
            nextPageLiveData = null
            if (_hasMore) {
                query = null
            }
        }

        fun reset() {
            unregister()
            _hasMore = true
            loadMoreState.value = LoadMoreState(
                isRunning = false,
                errorMessage = null
            )
        }
    }
}