package com.submission.app.story.story.views

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.submission.app.story.R
import com.submission.app.story.auth.models.AuthPref
import com.submission.app.story.auth.views.SignInActivity
import com.submission.app.story.databinding.ActivityListStoryBinding
import com.submission.app.story.shared.utils.ViewModelFactory
import com.submission.app.story.story.viewmodels.StoryViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "credential")

class ListStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListStoryBinding
    private lateinit var listStoryAdapter: ListStoryAdapter
    private lateinit var storyViewModel: StoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listStoryAdapter = ListStoryAdapter()
        supportActionBar?.apply {
            title = "StoryApp"
        }
        binding.apply {
            rvStories.setHasFixedSize(true)
            rvStories.adapter = listStoryAdapter
            rvStories.layoutManager = LinearLayoutManager(this@ListStoryActivity)
        }

        initViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.more -> {
                storyViewModel.signOut()

                val signIn = Intent(this@ListStoryActivity, SignInActivity::class.java)
                startActivity(signIn)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initViewModel() {
        val factory = ViewModelFactory.getInstance(AuthPref.getInstance(dataStore))
        storyViewModel = ViewModelProvider(this, factory)[StoryViewModel::class.java]

        storyViewModel.getCredential().observe(this) {credential ->
            storyViewModel.getStories("Bearer ${credential.token}")
        }

        storyViewModel.isLoading.observe(this) {
            handleLoading(it)
        }

        storyViewModel.storyResponse.observe(this) { stories ->
            if (stories != null) {
                listStoryAdapter.setData(stories)
            }
        }
    }

    private fun handleLoading(state: Boolean) {
        binding.apply {
            if (state) {
                loading.visibility = View.VISIBLE
                rvStories.visibility = View.GONE
            } else {
                loading.visibility = View.GONE
                rvStories.visibility = View.VISIBLE
            }
        }
    }
}