package com.example.dental

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var progressBar: ProgressBar
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            // Ensure action bar is hidden
            supportActionBar?.hide()
            
            setContentView(R.layout.activity_login)

            // Initialize Firebase Auth
            auth = FirebaseAuth.getInstance()
            db = FirebaseFirestore.getInstance()
            
            // Check if user is already logged in
            if (auth.currentUser != null) {
                checkUserRoleAndNavigate()
                return
            }

            // Initialize views
            emailInput = findViewById(R.id.email_input)
            passwordInput = findViewById(R.id.password_input)
            loginButton = findViewById(R.id.login_button)
            val forgotPassword = findViewById<TextView>(R.id.forgot_password)
            val signupLink = findViewById<TextView>(R.id.signup_link)
            progressBar = findViewById(R.id.login_progress)

            loginButton.setOnClickListener {
                loginUser()
            }

            forgotPassword.setOnClickListener {
                val intent = Intent(this, ForgotPasswordActivity::class.java)
                startActivity(intent)
            }

            signupLink.setOnClickListener {
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error initializing login: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
    
    private fun loginUser() {
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()

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

        // Show progress
        showLoading(true)

        // Firebase authentication
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                showLoading(false)
                
                if (task.isSuccessful) {
                    // Sign in success - check user role
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                    checkUserRoleAndNavigate()
                } else {
                    // Sign in failed
                    val errorMessage = task.exception?.message ?: "Authentication failed"
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
    }
    
    private fun checkUserRoleAndNavigate() {
        val userId = auth.currentUser?.uid ?: return
        
        // Check if user is a dentist
        db.collection("dentists").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // User is a dentist
                    navigateToDentistDashboard()
                } else {
                    // User is a patient
                    navigateToMain()
                }
            }
            .addOnFailureListener {
                // Default to patient dashboard if check fails
                navigateToMain()
            }
    }
    
    private fun navigateToDentistDashboard() {
        val intent = Intent(this, com.example.dental.view.DentistDashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
    
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            progressBar.visibility = View.VISIBLE
            loginButton.isEnabled = false
            emailInput.isEnabled = false
            passwordInput.isEnabled = false
        } else {
            progressBar.visibility = View.GONE
            loginButton.isEnabled = true
            emailInput.isEnabled = true
            passwordInput.isEnabled = true
        }
    }
    
    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
