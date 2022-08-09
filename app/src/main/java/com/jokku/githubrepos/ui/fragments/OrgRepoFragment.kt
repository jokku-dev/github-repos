package com.jokku.githubrepos.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jokku.githubrepos.R
import com.jokku.githubrepos.adapters.RepoAdapter
import com.jokku.githubrepos.databinding.FragmentOrgRepoBinding
import com.jokku.githubrepos.ui.GithubActivity
import com.jokku.githubrepos.ui.GithubViewModel
import com.jokku.githubrepos.util.Resource

class OrgRepoFragment : Fragment(R.layout.fragment_org_repo) {

    private lateinit var binding: FragmentOrgRepoBinding
    lateinit var viewModel: GithubViewModel
    lateinit var repoAdapter: RepoAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as GithubActivity).viewModel
        setupRecyclerView()
        observeOrgRepos()


    }

    private fun observeOrgRepos() {
        viewModel.orgRepos.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data.let { data ->
                        repoAdapter.listDiffer.submitList(data)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "An error occurred: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun setupRecyclerView() {
        repoAdapter = RepoAdapter()
        binding.rvOrgRepos.apply {
            adapter = repoAdapter
            layoutManager = LinearLayoutManager(activity)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == repoAdapter.itemCount - 1) {
                        viewModel.loadNextPage()
                    }
                }
            })
        }
    }

    private fun hideProgressBar() {
        binding.loadPageBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.loadPageBar.visibility = View.VISIBLE
    }
}

const val TAG = "OrgRepoFragment"