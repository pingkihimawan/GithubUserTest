package com.github.userlist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.userlist.data.model.GitUser
import com.github.userlist.databinding.ItemUserBinding
import kotlinx.android.synthetic.main.item_user.view.*

class MainAdapter(
    private val onItemClick: (user: GitUser) -> Unit
) : PagingDataAdapter<GitUser, MainAdapter.UsersViewHolder>(REPO_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        return UsersViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val userItem = getItem(position)
        if (userItem != null) {
            holder.bind(userItem)
            holder.itemView.setOnClickListener { onItemClick(userItem) }
        }
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<GitUser>() {
            override fun areItemsTheSame(oldItem: GitUser, newItem: GitUser): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: GitUser, newItem: GitUser): Boolean =
                oldItem == newItem
        }
    }


    inner class UsersViewHolder(
        private val binding: ItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: GitUser?) {
            binding.user = data
            Glide.with(itemView.context)
                .load(data?.avatarUrl)
                .into(binding.civUserAvatar)
        }

    }

}
