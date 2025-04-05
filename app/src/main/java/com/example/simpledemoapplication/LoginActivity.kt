package com.example.simpledemoapplication

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.example.simpledemoapplication.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //supportActionBar?.hide()
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        if (prefs.getBoolean("is_logged_in", false)) {
            navigateToHome()
        }
        binding.loginButton.isEnabled = false // initially disabled

        binding.email.addTextChangedListener(inputWatcher)
        binding.password.addTextChangedListener(inputWatcher)

        binding.loginButton.setOnClickListener {
            prefs.edit().putBoolean("is_logged_in", true).apply()
            navigateToHome()
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length in 8..15
    }

    private val inputWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            val isValidEmail = isEmailValid(email)
            val isValidPassword = isPasswordValid(password)

            // Optional: show errors
            binding.email.error = if (!isValidEmail && email.isNotEmpty()) "Invalid email" else null
            binding.password.error = if (!isValidPassword && password.isNotEmpty()) "8-15 characters" else null

            binding.loginButton.isEnabled = isValidEmail && isValidPassword
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    private fun navigateToHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()

    }
}