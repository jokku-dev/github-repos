package com.jokku.githubrepos.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.jokku.githubrepos.R
import com.jokku.githubrepos.databinding.FragmentOrgRepoBinding
import com.jokku.githubrepos.ui.GithubActivity
import com.jokku.githubrepos.ui.GithubViewModel
import com.jokku.githubrepos.ui.GithubViewModelProviderFactory

class OrgRepoFragment : Fragment(R.layout.fragment_org_repo) {

    private lateinit var binding: FragmentOrgRepoBinding

    lateinit var viewModel: GithubViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as GithubActivity).viewModel
    }
}