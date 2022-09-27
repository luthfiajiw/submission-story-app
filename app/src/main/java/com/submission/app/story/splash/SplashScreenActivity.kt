package com.submission.app.story.splash

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.submission.app.story.auth.models.AuthPref
import com.submission.app.story.auth.views.SignInActivity
import com.submission.app.story.databinding.ActivitySplashScreenBinding
import com.submission.app.story.shared.utils.ViewModelFactory
import java.util.*
import kotlin.concurrent.schedule

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "credential")

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var splashViewModel: SplashViewModel
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fullscreen()
        startAnimation()
        Timer().schedule(3000) {
            this@SplashScreenActivity.runOnUiThread {
                initViewModel()
            }
        }
    }

    private fun routeToSignIn() {
        val intent = Intent(this@SplashScreenActivity, SignInActivity::class.java)
        val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this@SplashScreenActivity,
            Pair(binding.appLogo, "logo")
        )

        startActivity(intent, optionsCompat.toBundle())
        finish()
    }

    private fun initViewModel() {
        splashViewModel = ViewModelProvider(
            this,
            ViewModelFactory(this@SplashScreenActivity.application, AuthPref.getInstance(dataStore))
        )[SplashViewModel::class.java]

        splashViewModel.getCredential().observe(this) {
            if (it.token.isEmpty()) {
                routeToSignIn()
            }
        }
    }

    private fun startAnimation() {
        ObjectAnimator.ofFloat(binding.appLogo, View.ALPHA, 1f).apply {
            duration = 3000
        }.start()
    }

    private fun fullscreen() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}