package com.submission.app.story.auth.views

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import com.submission.app.story.databinding.ActivitySignInBinding
import com.submission.app.story.shared.components.CustomButton
import com.submission.app.story.shared.components.TextField

class SignInActivity : AppCompatActivity() {
    private lateinit var btnSignin: CustomButton
    private lateinit var edEmail: TextField
    private lateinit var edPassword: TextField
    private lateinit var textRegister: TextView
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            edEmail = edLoginEmail
            edPassword = edLoginPassword
            this@SignInActivity.textRegister = textRegister
            this@SignInActivity.btnSignin = btnSignin
        }

        supportActionBar?.hide()
        startAnimation()
        handleChangedPassword()

        textRegister.setOnClickListener {
            val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun handleChangedPassword() {
        edPassword.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val stringLength = s?.toString()?.length ?: 0
                btnSignin.isEnabled = stringLength >= 6
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
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