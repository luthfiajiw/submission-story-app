package com.submission.app.story.auth.views

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.submission.app.story.auth.AuthViewModel
import com.submission.app.story.auth.models.AuthModel
import com.submission.app.story.auth.models.AuthPref
import com.submission.app.story.databinding.ActivitySignInBinding
import com.submission.app.story.shared.components.CustomButton
import com.submission.app.story.shared.components.TextField
import com.submission.app.story.shared.utils.ViewModelFactory
import com.submission.app.story.story.views.ListStoryActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "credential")

class SignInActivity : AppCompatActivity() {
    private lateinit var btnSignin: CustomButton
    private lateinit var edPassword: TextField
    private lateinit var edEmail: TextField
    private lateinit var textRegister: TextView
    private lateinit var authViewModel: AuthViewModel
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        bindView()
        initViewModel()
        startAnimation()
        handleLogin()

        textRegister.setOnClickListener {
            val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun bindView() {
        edEmail = binding.edLoginEmail
        edPassword = binding.edLoginPassword
        textRegister = binding.textRegister
        btnSignin = binding.btnSignin
    }

    private fun initViewModel() {
        val factory = ViewModelFactory.getInstance(AuthPref.getInstance(dataStore))
        authViewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]
    }

    private fun handleLogin() {
        btnSignin.setOnClickListener {
            authViewModel.onLogin(AuthModel(
                name = "",
                email = edEmail.text.toString(),
                password = edPassword.text.toString()
            ))
        }

        authViewModel.loginResponse.observe(this) {
            it.getContentIfNotHandled()?.let { response ->
                Toast.makeText(this@SignInActivity, response.message, Toast.LENGTH_SHORT).show()
                if (!response.error) {
                    val listStory = Intent(this@SignInActivity, ListStoryActivity::class.java)
                    startActivity(listStory)
                    finish()
                }
            }
        }

        authViewModel.genericResponse.observe(this) {
            it.getContentIfNotHandled()?.let { response ->
                Toast.makeText(this@SignInActivity, response.message, Toast.LENGTH_SHORT).show()
            }
        }

        authViewModel.isLoading.observe(this) { loading ->
            btnSignin.isEnabled = !loading
        }
    }

    private fun startAnimation() {
        val title = ObjectAnimator.ofFloat(binding.title, View.ALPHA, 1f).setDuration(500)
        val signinText = ObjectAnimator.ofFloat(binding.signinToContinue, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(500)
        val btnSignin = ObjectAnimator.ofFloat(binding.btnSignin, View.ALPHA, 1f).setDuration(500)
        val textRegister = ObjectAnimator.ofFloat(binding.textRegister, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playTogether(title, signinText, email, password, btnSignin, textRegister)
        }.start()
    }
}