package com.submission.app.story.story.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.submission.app.story.databinding.ActivityDetailStoryBinding
import com.submission.app.story.story.Story

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailStoryBinding
    private lateinit var story : Story

    companion object {
        const val EXTRA_STORY = "extra_story"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = "Detail Story"
            setDisplayHomeAsUpEnabled(true)
        }

        story = intent.getParcelableExtra<Story>(EXTRA_STORY) as Story
        binding.apply {
            name.text = story.name
            description.text = story.description
            Glide.with(this@DetailStoryActivity)
                .load(story.photoUrl)
                .into(image)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}