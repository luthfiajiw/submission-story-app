package com.submission.app.story.splash

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.submission.app.story.auth.views.SignInActivity
import com.submission.app.story.databinding.ActivitySplashScreenBinding
import java.util.*
import kotlin.concurrent.schedule

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fullscreen()
        startAnimation()
        Timer().schedule(3000) {
            this@SplashScreenActivity.runOnUiThread {
                routeToSignIn()
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