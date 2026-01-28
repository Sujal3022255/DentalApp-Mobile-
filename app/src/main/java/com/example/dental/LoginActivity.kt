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
import com.example.dental.view.AdminDashboardActivity
import com.example.dental.view.DentistDashboardActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    
    private val authViewModel: AuthViewModel by viewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            // Ensure action bar is hidden
            supportActionBar?.hide()
            
            setContentView(R.layout.activity_login)

            // Initialize Firebase
            auth = FirebaseAuth.getInstance()
            db = FirebaseFirestore.getInstance()

            // Initialize views
            emailInput = findViewById(R.id.email_input)
            passwordInput = findViewById(R.id.password_input)
            loginButton = findViewById(R.id.login_button)
            val forgotPassword = findViewById<TextView>(R.id.forgot_password)
            val signupLink = findViewById<TextView>(R.id.signup_link)
            progressBar = findViewById(R.id.login_progress)

            // Observe auth state
            authViewModel.authState.observe(this) { state ->
                showLoading(state.isLoading)
                
                if (state.isSuccess && state.user != null) {
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                    checkUserRoleAndNavigate()
                }
                
                state.error?.let { error ->
                    Toast.makeText(this, error, Toast.LENGTH_LONG).show()
                }
            }

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

        // Login using ViewModel
        authViewModel.login(email, password)
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
    
    private fun checkUserRoleAndNavigate() {
        val userId = auth.currentUser?.uid ?: return
        
        showLoading(true)
        
        // Check if user is admin
        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                showLoading(false)
                val role = document.getString("role")?.lowercase() ?: "patient"
                
                when (role) {
                    "admin" -> navigateToAdminDashboard()
                    "dentist" -> navigateToDentistDashboard()
                    else -> navigateToPatientDashboard()
                }
            }
            .addOnFailureListener { e ->
                showLoading(false)
                // If role not found, default to patient dashboard
                Toast.makeText(this, "Role not found, logging in as patient", Toast.LENGTH_SHORT).show()
                navigateToPatientDashboard()
            }
    }
    
    private fun navigateToAdminDashboard() {
        val intent = Intent(this, AdminDashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
    
    private fun navigateToDentistDashboard() {
        val intent = Intent(this, DentistDashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
    
    private fun navigateToPatientDashboard() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
