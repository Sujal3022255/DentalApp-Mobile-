package com.example.dental

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    
    private lateinit var auth: FirebaseAuth
    private lateinit var emailInput: EditText
    private lateinit var resetButton: Button
    private lateinit var progressBar: ProgressBar
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            setContentView(R.layout.activity_forgot_password)
            
            // Enable back button in action bar with white color
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel)

            // Initialize Firebase Auth
            auth = FirebaseAuth.getInstance()
            
            emailInput = findViewById(R.id.forgot_email_input)
            resetButton = findViewById(R.id.reset_button)
            progressBar = findViewById(R.id.forgot_progress)
            val backToLogin = findViewById<TextView>(R.id.back_to_login)

            resetButton.setOnClickListener {
                resetPassword()
            }

            backToLogin.setOnClickListener {
                finish()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error initializing forgot password: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
    
    private fun resetPassword() {
        val email = emailInput.text.toString().trim()

        // Validation
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

        // Show progress
        showLoading(true)

        // Send password reset email
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                showLoading(false)
                
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Password reset link sent to $email. Please check your inbox.",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                } else {
                    val errorMessage = task.exception?.message ?: "Failed to send reset email"
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
    }
    
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            progressBar.visibility = View.VISIBLE
            resetButton.isEnabled = false
            emailInput.isEnabled = false
        } else {
            progressBar.visibility = View.GONE
            resetButton.isEnabled = true
            emailInput.isEnabled = true
        }
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
