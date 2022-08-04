package com.jokku.githubrepos.db

import android.util.SparseIntArray
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.jokku.githubrepos.vo.Contributor
import com.jokku.githubrepos.vo.Repo
import com.jokku.githubrepos.vo.RepoSearchResult

@Dao
interface RepoDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(vararg repos: Repo)

    @Insert(onConflict = REPLACE)
    suspend fun insertContributors(contributors: List<Contributor>)

    @Insert(onConflict = REPLACE)
    suspend fun insertRepos(repositories: List<Repo>)

    @Insert(onConflict = IGNORE)
    suspend fun createRepoIfNotExist(repo: Repo): Long

    @Insert(onConflict = REPLACE)
    suspend fun insert(result: RepoSearchResult)

    @Query(
        """SELECT login, avatar_url, repoName, repoOwner, contributions FROM Contributor
        WHERE repoName = :name AND repoOwner = :owner ORDER BY contributions DESC"""
    )
    fun loadContributors(owner: String, name: String): LiveData<List<Contributor>>

    @Query("SELECT * FROM Repo WHERE owner_login = :owner ORDER BY stargazers_count DESC")
    fun loadRepositories(owner: String): LiveData<List<Repo>>

    @Query("SELECT * FROM RepoSearchResult WHERE `query` = :query")
    fun search(query: String): LiveData<RepoSearchResult?>

    @Query("SELECT * FROM RepoSearchResult WHERE `query` = :query")
    fun findSearchResult(query: String): RepoSearchResult?

    fun loadOrdered(repoIds: List<Int>): LiveData<List<Repo>> {
        val order = SparseIntArray()
        repoIds.withIndex().forEach {
            order.put(it.value, it.index)
        }
        return loadById(repoIds).map { repos ->
            repos.sortedWith(compareBy { order.get(it.id) })
        }
    }

    @Query("SELECT * FROM Repo WHERE id in (:repoIds)")
    fun loadById(repoIds: List<Int>): LiveData<List<Repo>>

}