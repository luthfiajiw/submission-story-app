package com.submission.app.story.auth.views

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.submission.app.story.auth.AuthViewModel
import com.submission.app.story.auth.models.AuthModel
import com.submission.app.story.auth.models.AuthPref
import com.submission.app.story.databinding.ActivitySignUpBinding
import com.submission.app.story.shared.components.CustomButton
import com.submission.app.story.shared.components.TextField
import com.submission.app.story.shared.utils.ViewModelFactory
import java.util.*
import kotlin.concurrent.schedule

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "credential")

class SignUpActivity : AppCompatActivity() {
    private lateinit var edName : TextField
    private lateinit var edEmail : TextField
    private lateinit var edPassword : TextField
    private lateinit var btnRegister : CustomButton
    private lateinit var btnBack: ImageView
    private lateinit var authViewModel: AuthViewModel
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        bindView()
        initViewModel()
        handleChangedPassword()
        handleRegister()

        btnBack.setOnClickListener { finish() }
    }

    private fun initViewModel() {
        val factory = ViewModelFactory.getInstance(AuthPref.getInstance(dataStore))
        authViewModel = ViewModelProvider(this@SignUpActivity, factory)[AuthViewModel::class.java]
    }

    private fun bindView() {
        edName = binding.edRegisterName
        edEmail = binding.edRegisterEmail
        edPassword = binding.edRegisterPassword
        btnBack = binding.btnBack
        btnRegister = binding.btnRegister
    }

    private fun handleRegister() {
        btnRegister.setOnClickListener {
            authViewModel.onRegister(AuthModel(
                name = edName.text.toString(),
                email = edEmail.text.toString(),
                password = edPassword.text.toString()
            ))
        }

        authViewModel.genericResponse.observe(this) {
            it.getContentIfNotHandled()?.let { response ->
                if (response.error) {
                    Toast.makeText(this@SignUpActivity, response.message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@SignUpActivity, response.message, Toast.LENGTH_SHORT).show()
                    Timer().schedule(1000) {
                        this@SignUpActivity.runOnUiThread { finish() }
                    }
                }
            }
        }

        authViewModel.isLoading.observe(this) { loading ->
            btnRegister.isEnabled = !loading && edPassword.text.toString().length > 6
        }
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