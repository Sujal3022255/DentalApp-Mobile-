package com.example.dental

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.dental.data.model.AuthState
import com.example.dental.viewmodel.AuthViewModel

class SignUpActivity : AppCompatActivity() {
    
    private lateinit var nameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var confirmPasswordInput: EditText
    private lateinit var signupButton: Button
    private lateinit var progressBar: ProgressBar
    
    private val authViewModel: AuthViewModel by viewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            setContentView(R.layout.activity_signup)
            
            // Enable back button in action bar with white color
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel)
            
            nameInput = findViewById(R.id.signup_name_input)
            emailInput = findViewById(R.id.signup_email_input)
            passwordInput = findViewById(R.id.signup_password_input)
            confirmPasswordInput = findViewById(R.id.signup_confirm_password_input)
            signupButton = findViewById(R.id.signup_button)
            progressBar = findViewById(R.id.signup_progress)
            val alreadyHaveAccount = findViewById<TextView>(R.id.already_have_account)

            // Observe auth state
            authViewModel.authState.observe(this) { state ->
                showLoading(state.isLoading)
                
                if (state.isSuccess && state.user != null) {
                    Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show()
                    navigateToMain()
                }
                
                state.error?.let { error ->
                    Toast.makeText(this, error, Toast.LENGTH_LONG).show()
                }
            }

            signupButton.setOnClickListener {
                signUpUser()
            }

            alreadyHaveAccount.setOnClickListener {
                finish()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error initializing signup: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
    
    private fun signUpUser() {
        val name = nameInput.text.toString().trim()
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()
        val confirmPassword = confirmPasswordInput.text.toString().trim()

        // Validation
        if (name.isEmpty()) {
            nameInput.error = "Name is required"
            nameInput.requestFocus()
            return
        }
        
        if (email.isEmpty()) {
            emailInput.error = "Email is required"
            emailInput.requestFocus()
            return
        }
        
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.error = "Please enter a valid email"
            emailInput.requestFocus()
            return
        }
        
        if (password.isEmpty()) {
            passwordInput.error = "Password is required"
            passwordInput.requestFocus()
            return
        }
        
        if (password.length < 6) {
            passwordInput.error = "Password must be at least 6 characters"
            passwordInput.requestFocus()
            return
        }
        
        if (confirmPassword.isEmpty()) {
            confirmPasswordInput.error = "Please confirm your password"
            confirmPasswordInput.requestFocus()
            return
        }
        
        if (password != confirmPassword) {
            confirmPasswordInput.error = "Passwords do not match"
            confirmPasswordInput.requestFocus()
            return
        }

        // Sign up using ViewModel
        authViewModel.signUp(name, email, password)
    }
    
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            progressBar.visibility = View.VISIBLE
            signupButton.isEnabled = false
            nameInput.isEnabled = false
            emailInput.isEnabled = false
            passwordInput.isEnabled = false
            confirmPasswordInput.isEnabled = false
        } else {
            progressBar.visibility = View.GONE
            signupButton.isEnabled = true
            nameInput.isEnabled = true
            emailInput.isEnabled = true
            passwordInput.isEnabled = true
            confirmPasswordInput.isEnabled = true
        }
    }
    
    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
