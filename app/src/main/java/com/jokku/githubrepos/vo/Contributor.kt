package com.jokku.githubrepos.vo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE

@Entity(
    primaryKeys = ["repoName", "repoOwner", "login"],
    foreignKeys = [ForeignKey(
        entity = Repo::class,
        parentColumns = ["name", "owner_login"],
        childColumns = ["repoName", "repoOwner"],
        onUpdate = CASCADE,
        deferred = true
    )]
)
data class Contributor(
    val login: String,
    val contributions: Int,
    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String?
) {
    lateinit var repoName: String
    lateinit var repoOwner: String
}
