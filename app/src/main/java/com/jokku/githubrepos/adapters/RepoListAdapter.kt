package com.jokku.githubrepos.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jokku.githubrepos.R
import com.jokku.githubrepos.databinding.RepoItemBinding
import com.jokku.githubrepos.vo.Repo

class RepoListAdapter : RecyclerView.Adapter<RepoListAdapter.RepoViewHolder>() {

    inner class RepoViewHolder(private val binding: RepoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(repo: Repo) = with(binding) {
            tvName.text = repo.name
            tvStars.text = repo.stars.toString()
            tvDesc.text = repo.description

        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Repo>() {
        override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
            return oldItem.owner == newItem.owner && oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean {
            return oldItem.description == newItem.description && oldItem.stars == newItem.stars
        }

    }

    private val differ = AsyncListDiffer(this, differCallback)

    private var onItemClickListener: ((Repo) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val binding = RepoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        val repo = differ.currentList[position]
        holder.bind(repo)
        setOnItemClickListener {
            onItemClickListener?.let { function ->
                function(repo) }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private fun setOnItemClickListener(listener: (Repo) -> Unit) {
        onItemClickListener = listener
    }
}