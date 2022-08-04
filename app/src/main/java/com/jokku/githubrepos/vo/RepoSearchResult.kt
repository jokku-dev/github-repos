package com.jokku.githubrepos.vo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.TypeConverters
import com.jokku.githubrepos.db.GithubTypeConverters

@Entity(primaryKeys = ["query"])
@TypeConverters(GithubTypeConverters::class)
data class RepoSearchResult(
    val query: String,
    @ColumnInfo(name = "repo_ids")
    val repoIds: List<Int>,
    @ColumnInfo(name = "total_count")
    val totalCount: Int,
    val next: Int?
)
