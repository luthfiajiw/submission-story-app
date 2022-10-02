package com.submission.app.story.story.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.submission.app.story.R
import com.submission.app.story.databinding.ItemRowStoryBinding
import com.submission.app.story.story.Story
import com.submission.app.story.story.StoryResponse

class ListStoryAdapter() : RecyclerView.Adapter<ListStoryAdapter.ListViewHolder>() {
    private var mData = StoryResponse()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setData(storyResponse: StoryResponse) {
        mData = storyResponse
        notifyDataSetChanged()
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemRowStoryBinding.bind(itemView)

        fun bind(story: Story) {
            with(itemView) {
                binding.apply {
                    name.text = story.name
                    Glide.with(context)
                        .load(story.photoUrl)
                        .into(image)
                }

                setOnClickListener{
                    onItemClickCallback.onItemClicked(story)
                }
            }
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Story)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListStoryAdapter.ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_story, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListStoryAdapter.ListViewHolder, position: Int) {
        val story = mData.listStory[position]

        holder.bind(story)
    }

    override fun getItemCount(): Int = mData.listStory.size
}