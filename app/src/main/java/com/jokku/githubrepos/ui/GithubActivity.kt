package com.jokku.githubrepos.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.jokku.githubrepos.R
import com.jokku.githubrepos.db.GithubDatabase
import com.jokku.githubrepos.repository.GithubRepository

class GithubActivity : AppCompatActivity() {

    lateinit var viewModel: GithubViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github)

        val githubRepository = GithubRepository(GithubDatabase(this))
        val viewModelProviderFactory = GithubViewModelProviderFactory(githubRepository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory)[GithubViewModel::class.java]
    }
}