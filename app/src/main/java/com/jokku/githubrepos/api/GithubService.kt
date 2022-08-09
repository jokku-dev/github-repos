package com.jokku.githubrepos.api

import com.jokku.githubrepos.vo.Contributor
import com.jokku.githubrepos.vo.Repo
import com.jokku.githubrepos.vo.RepoSearchResult
import com.jokku.githubrepos.vo.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {

    @GET("users/{login}")
    suspend fun getUser(
        @Path("login") login: String
    ): Response<User>

    @GET("users/{login}/repos")
    suspend fun getUserRepos(
        @Path("login") login: String
    ): Response<List<Repo>>

    @GET("repos/{owner}/{repo}")
    suspend fun getRepo(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Response<Repo>

    @GET("repos/{owner}/{repo}/contributors")
    suspend fun getContributors(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Response<List<Contributor>>

    @GET("search/repositories")
    fun searchOrgRepos(
        @Query("q") query: String,
        @Query("page") page: Int
    ): Call<RepoSearchResponse>
}