package com.jokku.githubrepos.vo

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity

@Entity(
    primaryKeys = ["name", "owner_login"]
)
data class Repo(
    val description: String?,
    @ColumnInfo(name = "full_name")
    val fullName: String,
    val id: Int,
    val name: String,
    @Embedded(prefix = "owner_")
    val owner: Owner,
    @ColumnInfo(name = "stargazers_count")
    val stars: Int,
) {
    data class Owner(
        val login: String,
        val url: String?
    )
}