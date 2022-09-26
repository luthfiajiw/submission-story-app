package com.submission.app.story.auth.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import android.widget.TextView
import com.submission.app.story.databinding.ActivitySignUpBinding
import com.submission.app.story.shared.components.CustomButton
import com.submission.app.story.shared.components.TextField

class SignUpActivity : AppCompatActivity() {
    private lateinit var edPassword : TextField
    private lateinit var btnRegister : CustomButton
    private lateinit var btnBack: ImageView
    private lateinit var textSignin : TextView
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        binding.apply {
            edPassword = edRegisterPassword
            this@SignUpActivity.textSignin = textSignin
            this@SignUpActivity.btnBack = btnBack
            this@SignUpActivity.btnRegister = btnRegister
        }

        handleChangedPassword()

        btnBack.setOnClickListener { finish() }
        textSignin.setOnClickListener { finish() }
    }

    private fun handleChangedPassword() {
        edPassword.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val stringLength = s?.toString()?.length ?: 0

                if (stringLength < 6) {
                    edPassword.error = "Password must be minimum 6 characters"
                    btnRegister.isEnabled = false
                } else {
                    btnRegister.isEnabled = true
                    edPassword.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }
}