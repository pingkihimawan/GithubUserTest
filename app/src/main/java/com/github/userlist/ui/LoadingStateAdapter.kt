package com.github.userlist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.userlist.databinding.NetworkStateItemBinding

class LoadingStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<LoadingStateAdapter.LoadStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)

    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder(
            NetworkStateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    inner class LoadStateViewHolder(val binding: NetworkStateItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) {
            val progress = binding.progressBarItem
            val retryBtn = binding.retyBtn
            val txtErrorMessage = binding.errorMsgItem
            if (loadState is LoadState.Loading) {
                progress.isVisible = true
                txtErrorMessage.isVisible = false
                retryBtn.isVisible = false

            } else {
                progress.isVisible = false
            }
            if (loadState is LoadState.Error) {
                txtErrorMessage.isVisible = true
                retryBtn.isVisible = true
                txtErrorMessage.text = loadState.error.localizedMessage
            }
            retryBtn.setOnClickListener {
                retry.invoke()
            }
        }
    }
}