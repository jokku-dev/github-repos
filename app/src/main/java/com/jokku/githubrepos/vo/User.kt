package com.jokku.githubrepos.vo

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["login"])
data class User(
    val login: String,
    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String?,
    val name: String?,
    val company: String?,
    @ColumnInfo(name = "repos_url")
    val reposUrl: String?,
    val blog: String?
)
