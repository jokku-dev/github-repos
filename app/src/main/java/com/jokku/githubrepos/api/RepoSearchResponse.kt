package com.jokku.githubrepos.api

import com.google.gson.annotations.SerializedName
import com.jokku.githubrepos.vo.Repo

data class RepoSearchResponse(
    @SerializedName("total_count")
    val total: Int = 0,
    @SerializedName("items")
    val items: List<Repo>
) {
    var nextPage: Int? = null
}
