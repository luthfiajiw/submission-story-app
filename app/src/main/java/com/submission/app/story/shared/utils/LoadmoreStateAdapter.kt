package com.submission.app.story.shared.utils

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.submission.app.story.databinding.ItemLoadmoreBinding

class LoadmoreStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<LoadmoreStateAdapter.LoadmoreStateViewHolder>() {
    class LoadmoreStateViewHolder(
        private val binding: ItemLoadmoreBinding,
        retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.retryButton.setOnClickListener { retry.invoke() }
        }
        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.errorMsg.text = loadState.error.localizedMessage
            }

            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.retryButton.isVisible = loadState is LoadState.Error
            binding.errorMsg.isVisible = loadState is LoadState.Error
        }
    }

    override fun onBindViewHolder(
        holder: LoadmoreStateAdapter.LoadmoreStateViewHolder,
        loadState: LoadState
    ) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadmoreStateAdapter.LoadmoreStateViewHolder {
        TODO("Not yet implemented")
    }
}